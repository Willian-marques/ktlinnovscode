package com.example.motoboyrecrutamento.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motoboyrecrutamento.data.local.AppDatabase
import com.example.motoboyrecrutamento.data.local.Motoboy
import com.example.motoboyrecrutamento.data.remote.RetrofitClient
import com.example.motoboyrecrutamento.data.repository.CandidaturaRepository
import com.example.motoboyrecrutamento.data.repository.VagaRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.motoboyrecrutamento.data.local.Vaga
import com.example.motoboyrecrutamento.data.firebase.FirestoreMotoboyService


/**
 * MEMBRO 3 - FASE 4: ViewModel do Motoboy
 * 
 * Gerencia a lógica de negócio para o perfil Motoboy:
 * - RF05: Enviar candidatura
 * - RF07: Anexar documentos (Firebase Storage)
 */
class MotoboyViewModel(application: Application) : AndroidViewModel(application) {

    private val database by lazy { AppDatabase.getDatabase(application) }
    private val vagaRepository by lazy { VagaRepository(
        vagaDao = database.vagaDao(),
        vagaApiService = RetrofitClient.vagaApiService
    ) }
    private val candidaturaRepository by lazy { CandidaturaRepository(
        candidaturaDao = database.candidaturaDao(),
        candidaturaApiService = RetrofitClient.candidaturaApiService,
        motoboyDao = database.motoboyDao(),
        usuarioDao = database.usuarioDao(),
        vagaDao = database.vagaDao()
    ) }
    
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val firestoreMotoboyService = FirestoreMotoboyService()
    
    // ID único do motoboy - busca do banco ou 0 se não encontrado
    private var _motoboyIdCache: Long? = null
    private suspend fun getMotoboyId(): Long {
        if (_motoboyIdCache != null) return _motoboyIdCache!!
        
        val firebaseUid = auth.currentUser?.uid ?: return 0L
        val uidHash = firebaseUid.hashCode().toLong().let { if (it < 0) -it else it }
        
        // Buscar motoboy existente no banco pelo usuarioId (hash do Firebase UID)
        val existingMotoboy = database.motoboyDao().getByUsuarioId(uidHash)
        
        _motoboyIdCache = existingMotoboy?.id ?: 0L
        return _motoboyIdCache!!
    }
    
    // Lista de vagas disponíveis
    private val _vagas = MutableStateFlow<List<Vaga>>(emptyList())
    val vagas: StateFlow<List<Vaga>> = _vagas
    
    // Vaga selecionada para detalhes
    private val _vagaSelecionada = MutableStateFlow<Vaga?>(null)
    val vagaSelecionada: StateFlow<Vaga?> = _vagaSelecionada
    
    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Estado de candidatura
    private val _candidaturaState = MutableStateFlow<CandidaturaState>(CandidaturaState.Idle)
    val candidaturaState: StateFlow<CandidaturaState> = _candidaturaState

    // NOVO: Indica se o motoboy já se candidatou a esta vaga
    private val _candidaturaExistente = MutableStateFlow(false)
    val candidaturaExistente: StateFlow<Boolean> = _candidaturaExistente
    
    // Estado de upload de documento
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState
    
    // Estado do perfil do motoboy
    private val _motoboyPerfil = MutableStateFlow<Motoboy?>(null)
    val motoboyPerfil: StateFlow<Motoboy?> = _motoboyPerfil
    
    // Estado de salvamento do perfil
    private val _perfilSaveState = MutableStateFlow<PerfilSaveState>(PerfilSaveState.Idle)
    val perfilSaveState: StateFlow<PerfilSaveState> = _perfilSaveState
    
    /**
     * Carrega as vagas abertas disponíveis
     */
    fun loadVagas() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Sincronizar com o Firestore (isRestaurante = false para buscar vagas abertas)
            vagaRepository.syncVagasFromFirestore(isRestaurante = false)
            
            // Observar o Flow do Room
            vagaRepository.getVagasAbertas().collect { vagasList ->
                _vagas.value = vagasList
                _isLoading.value = false
            }
        }
    }


    
    /**
     * Carrega os detalhes de uma vaga específica
     */
    fun loadVagaDetalhes(vagaId: Long) {
        viewModelScope.launch {
            try {
                val motoboyId = getMotoboyId()
                
                // Primeiro tenta sincronizar com o Firestore
                vagaRepository.syncVagasFromFirestore(isRestaurante = false)
                
                // Depois busca a vaga no banco local
                val vaga = database.vagaDao().getVagaByIdSync(vagaId)
                _vagaSelecionada.value = vaga

                // Verificar se já existe candidatura
                val candidatura = candidaturaRepository.getCandidaturaByVagaAndMotoboy(vagaId, motoboyId)
                _candidaturaExistente.value = candidatura != null
            } catch (e: Exception) {
                // Se falhar, tenta buscar apenas do banco local
                val vaga = database.vagaDao().getVagaByIdSync(vagaId)
                _vagaSelecionada.value = vaga
                
                val motoboyId = getMotoboyId()
                val candidatura = candidaturaRepository.getCandidaturaByVagaAndMotoboy(vagaId, motoboyId)
                _candidaturaExistente.value = candidatura != null
            }
        }
    }
    
    /**
     * RF05: Enviar candidatura para uma vaga
     */
    fun enviarCandidatura(vagaId: Long) {
        viewModelScope.launch {
            val motoboyId = getMotoboyId()
            
            // Regra de negócio: só permite uma candidatura por vaga
            if (_candidaturaExistente.value) {
                _candidaturaState.value = CandidaturaState.Error("Você já se candidatou a esta vaga.")
                return@launch
            }

            try {
                _candidaturaState.value = CandidaturaState.Loading

                val result = candidaturaRepository.enviarCandidatura(
                    vagaId = vagaId,
                    motoboyId = motoboyId
                )

                if (result.isSuccess) {
                    _candidaturaExistente.value = true // Atualiza o status após o sucesso // <-- ADICIONADO
                    _candidaturaState.value = CandidaturaState.Success("Candidatura enviada com sucesso!")
                } else {
                    _candidaturaState.value = CandidaturaState.Error(
                        result.exceptionOrNull()?.message ?: "Erro ao enviar candidatura"
                    )
                }
            } catch (e: Exception) {
                _candidaturaState.value = CandidaturaState.Error(
                    e.message ?: "Erro desconhecido ao enviar candidatura"
                )
            }
        }
    }
    
    /**
     * RF07: Fazer upload de documento para Firebase Storage
     */
    fun uploadDocumento(fileUri: Uri, documentType: String) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Loading
                
                val motoboyId = getMotoboyId()
                
                // Verificar se tem ID do motoboy
                if (motoboyId == 0L) {
                    _uploadState.value = UploadState.Error(
                        "Erro: Complete seu perfil antes de fazer upload de documentos"
                    )
                    return@launch
                }
                
                // Criar referência no Firebase Storage
                val storageRef = storage.reference
                val documentRef = storageRef.child("documentos/motoboy_${motoboyId}/${documentType}_${System.currentTimeMillis()}.jpg")
                
                // Fazer upload
                val uploadTask = documentRef.putFile(fileUri).await()
                
                // Obter URL do documento
                val downloadUrl = documentRef.downloadUrl.await()
                
                // TODO: Salvar URL no banco de dados (Fase 2)
                
                _uploadState.value = UploadState.Success(downloadUrl.toString())
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Object does not exist") == true -> 
                        "Erro no Firebase Storage. Configure as regras de segurança no Firebase Console:\n\nRegras → allow read, write: if request.auth != null;"
                    e.message?.contains("permission") == true || e.message?.contains("unauthorized") == true ->
                        "Sem permissão para fazer upload. Verifique as regras do Firebase Storage"
                    else -> 
                        "Erro ao fazer upload: ${e.message}"
                }
                _uploadState.value = UploadState.Error(errorMessage)
            }
        }
    }

    /**
     * Carrega os dados do perfil do motoboy (sincroniza do Firestore)
     */
    fun loadPerfil() {
        viewModelScope.launch {
            try {
                val firebaseUid = auth.currentUser?.uid
                if (firebaseUid == null) {
                    _motoboyPerfil.value = null
                    return@launch
                }
                
                // Buscar do Firestore primeiro
                val firestoreResult = firestoreMotoboyService.getMotoboy(firebaseUid)
                
                if (firestoreResult.isSuccess) {
                    val firestoreData = firestoreResult.getOrNull()
                    
                    if (firestoreData != null) {
                        // Atualizar banco local com dados do Firestore
                        val motoboyId = getMotoboyId()
                        val uidHash = firebaseUid.hashCode().toLong().let { if (it < 0) -it else it }
                        
                        val motoboyAtualizado = Motoboy(
                            id = if (motoboyId > 0) motoboyId else 0,
                            usuarioId = uidHash,
                            cnh = firestoreData["cnh"] as? String ?: "",
                            telefone = firestoreData["telefone"] as? String ?: "",
                            veiculo = firestoreData["veiculo"] as? String ?: "",
                            experienciaAnos = (firestoreData["experienciaAnos"] as? Long)?.toInt() ?: 0,
                            raioAtuacao = (firestoreData["raioAtuacao"] as? Double) ?: 0.0
                        )
                        
                        if (motoboyId > 0) {
                            database.motoboyDao().update(motoboyAtualizado)
                        } else {
                            val newId = database.motoboyDao().insert(motoboyAtualizado)
                            _motoboyIdCache = newId
                            _motoboyPerfil.value = motoboyAtualizado.copy(id = newId)
                            return@launch
                        }
                        
                        _motoboyPerfil.value = motoboyAtualizado
                    } else {
                        // Não existe no Firestore, buscar do banco local
                        val motoboyId = getMotoboyId()
                        val motoboy = database.motoboyDao().getMotoboyByIdSync(motoboyId)
                        _motoboyPerfil.value = motoboy
                    }
                } else {
                    // Erro ao buscar do Firestore, buscar do banco local
                    val motoboyId = getMotoboyId()
                    val motoboy = database.motoboyDao().getMotoboyByIdSync(motoboyId)
                    _motoboyPerfil.value = motoboy
                }
            } catch (e: Exception) {
                _motoboyPerfil.value = null
            }
        }
    }

    /**
     * Salva ou atualiza o perfil do motoboy (local e Firestore)
     */
    fun salvarPerfil(cnh: String, telefone: String, veiculo: String, experienciaAnos: Int, raioAtuacao: Double) {
        viewModelScope.launch {
            try {
                _perfilSaveState.value = PerfilSaveState.Loading
                
                val firebaseUid = auth.currentUser?.uid
                val firebaseUser = auth.currentUser
                
                if (firebaseUid == null || firebaseUser == null) {
                    _perfilSaveState.value = PerfilSaveState.Error("Usuário não autenticado")
                    return@launch
                }
                
                val motoboyId = getMotoboyId()
                val motoboyAtual = database.motoboyDao().getMotoboyByIdSync(motoboyId)
                
                val uidHash = firebaseUid.hashCode().toLong().let { if (it < 0) -it else it }
                
                if (motoboyAtual != null) {
                    // Atualizar motoboy existente
                    val motoboyAtualizado = motoboyAtual.copy(
                        cnh = cnh,
                        telefone = telefone,
                        veiculo = veiculo,
                        experienciaAnos = experienciaAnos,
                        raioAtuacao = raioAtuacao
                    )
                    database.motoboyDao().update(motoboyAtualizado)
                    _motoboyIdCache = motoboyAtualizado.id
                    _motoboyPerfil.value = motoboyAtualizado
                } else {
                    // Criar novo motoboy - deixa o Room gerar o ID automaticamente
                    val novoMotoboy = Motoboy(
                        id = 0, // Room vai gerar o ID automaticamente
                        usuarioId = uidHash, // Usa hash do Firebase UID como usuarioId
                        cnh = cnh,
                        telefone = telefone,
                        veiculo = veiculo,
                        experienciaAnos = experienciaAnos,
                        raioAtuacao = raioAtuacao
                    )
                    val newId = database.motoboyDao().insert(novoMotoboy)
                    _motoboyIdCache = newId
                    _motoboyPerfil.value = novoMotoboy.copy(id = newId)
                }
                
                // Salvar no Firestore
                val firestoreResult = firestoreMotoboyService.saveMotoboy(
                    motoboyId = firebaseUid,
                    nome = firebaseUser.displayName ?: "Nome não informado",
                    email = firebaseUser.email ?: "",
                    cnh = cnh,
                    telefone = telefone,
                    veiculo = veiculo,
                    experienciaAnos = experienciaAnos,
                    raioAtuacao = raioAtuacao
                )
                
                if (firestoreResult.isFailure) {
                    Log.e("MotoboyViewModel", "Erro ao salvar no Firestore: ${firestoreResult.exceptionOrNull()?.message}")
                    _perfilSaveState.value = PerfilSaveState.Error(
                        "Perfil salvo localmente, mas falhou ao sincronizar com o servidor"
                    )
                } else {
                    _perfilSaveState.value = PerfilSaveState.Success
                }
            } catch (e: Exception) {
                _perfilSaveState.value = PerfilSaveState.Error(e.message ?: "Erro ao salvar perfil")
            }
        }
    }
    
    /**
     * Reseta o estado de salvamento do perfil
     */
    fun resetPerfilSaveState() {
        _perfilSaveState.value = PerfilSaveState.Idle
    }

    // Estados Selados para Candidatura
    sealed class CandidaturaState {
        object Idle : CandidaturaState()
        object Loading : CandidaturaState()
        data class Success(val message: String) : CandidaturaState()
        data class Error(val message: String) : CandidaturaState()
    }

    // Estados Selados para Upload de Documento
    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val url: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }
    
    // Estados Selados para Salvamento de Perfil
    sealed class PerfilSaveState {
        object Idle : PerfilSaveState()
        object Loading : PerfilSaveState()
        object Success : PerfilSaveState()
        data class Error(val message: String) : PerfilSaveState()
    }
}
