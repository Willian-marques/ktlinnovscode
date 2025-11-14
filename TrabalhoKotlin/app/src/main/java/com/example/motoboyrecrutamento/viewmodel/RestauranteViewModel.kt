package com.example.motoboyrecrutamento.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motoboyrecrutamento.data.local.AppDatabase
import com.example.motoboyrecrutamento.data.remote.RetrofitClient
import com.example.motoboyrecrutamento.data.repository.VagaRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.motoboyrecrutamento.data.repository.CandidaturaRepository
import com.example.motoboyrecrutamento.data.local.Candidatura
import com.example.motoboyrecrutamento.data.local.Vaga
import com.example.motoboyrecrutamento.data.local.Motoboy
import com.example.motoboyrecrutamento.data.firebase.FirestoreMotoboyService



/**
 * MEMBRO 2 - FASE 3: ViewModel do Restaurante
 *
 * Gerencia a lógica de negócio para o perfil Restaurante:
 * - RF04: Publicar vagas
 * - RF06: Gerenciar candidaturas (parte da lógica)
 */
class RestauranteViewModel(application: Application) : AndroidViewModel(application) {

    private val database by lazy { AppDatabase.getDatabase(application) } // <-- ALTERADO
    private val vagaRepository by lazy { // <-- ALTERADO
        VagaRepository(
            vagaDao = database.vagaDao(),
            vagaApiService = RetrofitClient.vagaApiService
        )
    }

    private val candidaturaRepository by lazy { // <-- NOVO
        CandidaturaRepository(
            candidaturaDao = database.candidaturaDao(),
            candidaturaApiService = RetrofitClient.candidaturaApiService,
            motoboyDao = database.motoboyDao(),
            usuarioDao = database.usuarioDao(),
            vagaDao = database.vagaDao()
        )
    }

    private val auth = FirebaseAuth.getInstance()
    private val firestoreMotoboyService = FirestoreMotoboyService()
    
    // ID único do restaurante logado baseado no Firebase Auth UID
    private val restauranteId: String
        get() = auth.currentUser?.uid ?: ""

    private val _vagas = MutableStateFlow<List<Vaga>>(emptyList())
    val vagas: StateFlow<List<Vaga>> = _vagas

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de publicação de vaga
    private val _postVagaState = MutableStateFlow<PostVagaState>(PostVagaState.Idle)
    val postVagaState: StateFlow<PostVagaState> = _postVagaState

    private val _candidaturas = MutableStateFlow<List<Candidatura>>(emptyList())
    val candidaturas: StateFlow<List<Candidatura>> = _candidaturas // <-- NOVO

    private val _motoboyDetalhes = MutableStateFlow<Motoboy?>(null)
    val motoboyDetalhes: StateFlow<Motoboy?> = _motoboyDetalhes
    /**
     * Carrega as vagas do restaurante logado
     */
    fun loadVagas() {
        viewModelScope.launch {
            _isLoading.value = true

            // Sincronizar candidaturas do restaurante antes de carregar as vagas
            candidaturaRepository.syncCandidaturasByRestaurante(restauranteId)

            // Sincronizar com o Firestore (isRestaurante = true para buscar apenas suas vagas)
            vagaRepository.syncVagasFromFirestore(isRestaurante = true)

            // Observar o Flow do Room - APENAS vagas do restaurante logado
            vagaRepository.getVagasByRestaurante(restauranteId).collect { vagasList ->
                _vagas.value = vagasList
                _isLoading.value = false
            }
        }
    }

    /**
     * RF04: Publicar uma nova vaga
     */
    fun postVaga(
        titulo: String,
        descricao: String,
        salario: Double,
        requisitos: List<String>
    ) {
        viewModelScope.launch {
            try {
                _postVagaState.value = PostVagaState.Loading

                val result = vagaRepository.postVaga(
                    titulo = titulo,
                    descricao = descricao,
                    salario = salario,
                    requisitos = requisitos,
                    restauranteId = restauranteId
                )

                if (result.isSuccess) {
                    _postVagaState.value = PostVagaState.Success
                    // Recarregar vagas
                    loadVagas()
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Erro ao publicar vaga"
                    val userMessage = when {
                        errorMessage.contains("PERMISSION_DENIED") || errorMessage.contains("permission") -> 
                            "Erro de permissão no Firebase. Configure as regras do Firestore para permitir leitura/escrita."
                        errorMessage.contains("UNAUTHENTICATED") -> 
                            "Usuário não autenticado. Faça login novamente."
                        else -> errorMessage
                    }
                    _postVagaState.value = PostVagaState.Error(userMessage)
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Erro desconhecido ao publicar vaga"
                val userMessage = when {
                    errorMessage.contains("PERMISSION_DENIED") || errorMessage.contains("permission") -> 
                        "Erro de permissão no Firebase. Configure as regras do Firestore para permitir leitura/escrita."
                    errorMessage.contains("UNAUTHENTICATED") -> 
                        "Usuário não autenticado. Faça login novamente."
                    else -> errorMessage
                }
                _postVagaState.value = PostVagaState.Error(userMessage)
            }
        }
    }

    /**
     * RF05: Excluir uma vaga
     */
    fun deleteVaga(vagaId: Long) {
        viewModelScope.launch {
            Log.d("RestauranteViewModel", "Tentando excluir vaga ID: $vagaId")
            _isLoading.value = true
            val result = vagaRepository.deleteVaga(vagaId)
            _isLoading.value = false

            if (result.isFailure) {
                Log.e("RestauranteViewModel", "Erro ao excluir vaga: ${result.exceptionOrNull()?.message}")
            } else {
                Log.d("RestauranteViewModel", "Vaga excluída com sucesso")
            }
        }
    }


    fun loadMotoboyDetalhes(motoboyId: Long) {
        viewModelScope.launch {
            try {
                println("DEBUG: Tentando carregar motoboy com ID: $motoboyId")
                
                // Primeiro buscar do banco local
                val motoboy = database.motoboyDao().getMotoboyByIdSync(motoboyId)
                println("DEBUG: Motoboy encontrado no banco local: $motoboy")
                
                if (motoboy != null) {
                    _motoboyDetalhes.value = motoboy
                    
                    // Tentar sincronizar do Firestore em background
                    // O motoboyId local não é o mesmo que o Firebase UID
                    // Precisamos buscar pelo usuarioId que é o hash do Firebase UID
                    // Por isso, vamos apenas usar o que já temos localmente
                } else {
                    println("ERRO: Motoboy não encontrado no banco local. ID: $motoboyId")
                    _motoboyDetalhes.value = null
                }
            } catch (e: Exception) {
                println("ERRO ao carregar motoboy: ${e.message}")
                e.printStackTrace()
                _motoboyDetalhes.value = null
            }
        }
    }

    fun loadCandidaturas(vagaId: Long) {
        viewModelScope.launch {
            // Sincronizar candidaturas do Firestore
            Log.d("RestauranteViewModel", "Sincronizando candidaturas da vaga $vagaId")
            candidaturaRepository.syncCandidaturasByVaga(vagaId)
            
            // Observar candidaturas do banco local (Room)
            candidaturaRepository.getCandidaturasByVaga(vagaId).collect { candidaturasList ->
                Log.d("RestauranteViewModel", "Candidaturas carregadas: ${candidaturasList.size}")
                _candidaturas.value = candidaturasList
            }
        }
    }
    fun encerrarVaga(vagaId: Long) {
        viewModelScope.launch {
            android.util.Log.d("RestauranteViewModel", "Tentando encerrar vaga ID: $vagaId, restauranteId: $restauranteId")
            _isLoading.value = true
            val result = vagaRepository.updateVagaStatus(vagaId, "encerrada", restauranteId)
            _isLoading.value = false

            if (result.isFailure) {
                android.util.Log.e("RestauranteViewModel", "Erro ao encerrar vaga: ${result.exceptionOrNull()?.message}")
            } else {
                android.util.Log.d("RestauranteViewModel", "Vaga encerrada com sucesso!")
            }
            // loadVagas() será chamado automaticamente pois o Room Flow será atualizado
        }
    }

    /**
     * NOVO: Reativar uma vaga (alterar status para "aberta")
     */
    fun reativarVaga(vagaId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = vagaRepository.updateVagaStatus(vagaId, "aberta", restauranteId)
            _isLoading.value = false

            if (result.isFailure) {
                // Tratar erro
                println("Erro ao reativar vaga: ${result.exceptionOrNull()?.message}")
            }
            // loadVagas() será chamado automaticamente pois o Room Flow será atualizado
        }
    }


    // Estados Selados para Publicação de Vaga
    sealed class PostVagaState {
        object Idle : PostVagaState()
        object Loading : PostVagaState()
        object Success : PostVagaState()
        data class Error(val message: String) : PostVagaState()
    }
}
