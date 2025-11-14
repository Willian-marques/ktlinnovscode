package com.example.motoboyrecrutamento.data.repository

import android.util.Log
import com.example.motoboyrecrutamento.data.local.Candidatura
import com.example.motoboyrecrutamento.data.local.CandidaturaDao
import com.example.motoboyrecrutamento.data.local.MotoboyDao
import com.example.motoboyrecrutamento.data.local.UsuarioDao
import com.example.motoboyrecrutamento.data.local.VagaDao
import com.example.motoboyrecrutamento.data.remote.CandidaturaApiService
import com.example.motoboyrecrutamento.data.remote.CandidaturaDTO
import com.example.motoboyrecrutamento.data.remote.PostCandidaturaRequest
import com.example.motoboyrecrutamento.data.firebase.FirestoreCandidaturaService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * FASE 3: Repository para Candidaturas
 *
 * Implementa o padrão Repository e a lógica de "Single Source of Truth":
 * - Room é a fonte única de dados para a UI
 * - Firestore sincroniza dados com outros usuários
 *
 * MEMBRO 3: Este repository é usado principalmente pelo Motoboy
 * para se candidatar a vagas e gerenciar suas candidaturas.
 */
class CandidaturaRepository(
    private val candidaturaDao: CandidaturaDao,
    private val candidaturaApiService: CandidaturaApiService,
    private val motoboyDao: MotoboyDao,
    private val usuarioDao: UsuarioDao,
    private val vagaDao: VagaDao,
    private val firestoreService: FirestoreCandidaturaService = FirestoreCandidaturaService(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /**
     * Retorna um Flow de candidaturas de um motoboy específico.
     */
    fun getCandidaturasByMotoboy(motoboyId: Long): Flow<List<Candidatura>> {
        return candidaturaDao.getCandidaturasByMotoboy(motoboyId)
    }

    /**
     * Retorna um Flow de candidaturas de uma vaga específica.
     */
    fun getCandidaturasByVaga(vagaId: Long): Flow<List<Candidatura>> {
        return candidaturaDao.getCandidaturasByVaga(vagaId)
    }

    suspend fun getCandidaturaByVagaAndMotoboy(vagaId: Long, motoboyId: Long): Candidatura? { // <-- NOVO MÉTODO
        return candidaturaDao.getCandidaturaByVagaAndMotoboy(vagaId, motoboyId) // <-- NOVO MÉTODO
    }
    /**
     * Sincroniza candidaturas do motoboy da API com o banco local.
     */
    suspend fun syncCandidaturasByMotoboy(motoboyId: Long): Result<Unit> {
        return try {
            val response = candidaturaApiService.getCandidaturasByMotoboy(motoboyId)

            if (response.isSuccessful) {
                val candidaturasDTO = response.body() ?: emptyList()

                // Converter DTOs para entidades Room
                val candidaturas = candidaturasDTO.map { dto ->
                    Candidatura(
                        id = dto.id ?: 0,
                        vagaId = dto.vagaId,
                        motoboyId = dto.motoboyId,
                        dataCandidatura = dto.dataCandidatura,
                        status = dto.status
                    )
                }

                // Substituir cache local
                candidaturaDao.insertAll(candidaturas)

                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao buscar candidaturas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * RF05: Enviar candidatura (Motoboy)
     * Salva no Firestore para o restaurante receber
     */
    suspend fun enviarCandidatura(vagaId: Long, motoboyId: Long): Result<Candidatura> {
        return try {
            Log.d("CandidaturaRepository", "Enviando candidatura: vagaId=$vagaId, motoboyId=$motoboyId")
            
            // Verificar se já existe candidatura (chave primária composta)
            val candidaturaExistente = candidaturaDao.getCandidaturaByVagaAndMotoboy(vagaId, motoboyId)
            if (candidaturaExistente != null) {
                Log.d("CandidaturaRepository", "Candidatura já existe, retornando a existente")
                return Result.success(candidaturaExistente)
            }
            
            // 1. Buscar dados do motoboy
            val motoboy = motoboyDao.getMotoboyByIdSync(motoboyId)
                ?: return Result.failure(Exception("Motoboy não encontrado"))
            
            // 2. Pegar dados do Firebase Auth (usuário atual)
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuário não autenticado"))
            
            val motoboyNome = currentUser.displayName ?: "Sem nome"
            val motoboyEmail = currentUser.email ?: "Sem email"
            
            Log.d("CandidaturaRepository", "Motoboy encontrado: $motoboyNome")
            
            // 3. Buscar dados da vaga para pegar o firestoreId
            val vaga = vagaDao.getVagaByIdSync(vagaId)
                ?: return Result.failure(Exception("Vaga não encontrada"))
            
            val vagaFirestoreId = vaga.firestoreId
                ?: return Result.failure(Exception("Vaga sem firestoreId"))
            
            Log.d("CandidaturaRepository", "Vaga encontrada: firestoreId=$vagaFirestoreId")
            
            // 4. Salvar no Firestore
            val firestoreResult = firestoreService.createCandidatura(
                vagaId = vagaFirestoreId,
                motoboyId = currentUser.uid,
                motoboyNome = motoboyNome,
                motoboyEmail = motoboyEmail,
                motoboyTelefone = motoboy.telefone
            )
            
            if (firestoreResult.isFailure) {
                Log.e("CandidaturaRepository", "Erro ao salvar no Firestore: ${firestoreResult.exceptionOrNull()?.message}")
                return Result.failure(firestoreResult.exceptionOrNull()!!)
            }
            
            val firestoreId = firestoreResult.getOrNull()!!
            Log.d("CandidaturaRepository", "Candidatura salva no Firestore com ID: $firestoreId")
            
            // 5. Salvar no cache local (única vez)
            val dataCandidatura = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date())
            
            val candidatura = Candidatura(
                id = System.currentTimeMillis(), // Usar timestamp como ID único
                vagaId = vagaId,
                motoboyId = motoboyId,
                dataCandidatura = dataCandidatura,
                status = "pendente",
                firestoreId = firestoreId,
                motoboyNome = motoboyNome,
                motoboyEmail = motoboyEmail,
                motoboyTelefone = motoboy.telefone
            )

            candidaturaDao.insert(candidatura)
            Log.d("CandidaturaRepository", "Candidatura salva no Room")

            Result.success(candidatura)
        } catch (e: Exception) {
            Log.e("CandidaturaRepository", "Erro ao enviar candidatura: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Sincronizar candidaturas de uma vaga do Firestore
     * Usado pelo Restaurante para ver candidaturas
     */
    suspend fun syncCandidaturasByVaga(vagaId: Long): Result<Unit> {
        return try {
            Log.d("CandidaturaRepository", "Sincronizando candidaturas da vaga: $vagaId")
            
            // Buscar vaga para pegar firestoreId
            val vaga = vagaDao.getVagaByIdSync(vagaId)
                ?: return Result.failure(Exception("Vaga não encontrada"))
            
            val vagaFirestoreId = vaga.firestoreId
                ?: return Result.failure(Exception("Vaga sem firestoreId"))
            
            // Buscar candidaturas do Firestore
            val firestoreResult = firestoreService.getCandidaturasByVaga(vagaFirestoreId)
            
            if (firestoreResult.isFailure) {
                Log.e("CandidaturaRepository", "Erro ao buscar do Firestore: ${firestoreResult.exceptionOrNull()?.message}")
                return Result.failure(firestoreResult.exceptionOrNull()!!)
            }
            
            val candidaturas = firestoreResult.getOrNull()!!
            Log.d("CandidaturaRepository", "Encontradas ${candidaturas.size} candidaturas no Firestore")
            
            // Salvar no cache local SOMENTE se não existir
            candidaturas.forEach { candidatura ->
                val existente = candidaturaDao.getCandidaturaByVagaAndMotoboy(
                    candidatura.vagaId,
                    candidatura.motoboyId
                )
                
                if (existente == null) {
                    candidaturaDao.insert(candidatura)
                    Log.d("CandidaturaRepository", "Nova candidatura inserida: ${candidatura.motoboyNome}")
                } else {
                    Log.d("CandidaturaRepository", "Candidatura já existe, pulando: ${candidatura.motoboyNome}")
                }
            }
            
            Log.d("CandidaturaRepository", "Candidaturas sincronizadas com sucesso")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CandidaturaRepository", "Erro ao sincronizar candidaturas: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * RF05: Cancelar candidatura (Motoboy)
     */
    suspend fun updateCandidaturaStatus(candidatura: Candidatura, status: String): Result<Unit> {
        return try {
            val candidaturaAtualizada = candidatura.copy(status = status)

            val candidaturaDTO = CandidaturaDTO(
                id = candidaturaAtualizada.id,
                vagaId = candidaturaAtualizada.vagaId,
                motoboyId = candidaturaAtualizada.motoboyId,
                dataCandidatura = candidaturaAtualizada.dataCandidatura,
                status = candidaturaAtualizada.status
            )

            val response = candidaturaApiService.updateCandidatura(candidaturaAtualizada.id, candidaturaDTO)

            if (response.isSuccessful) {
                candidaturaDao.update(candidaturaAtualizada)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao atualizar candidatura: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * NOVO: Sincroniza candidaturas de todas as vagas do restaurante da API com o banco local.
     */
    suspend fun syncCandidaturasByRestaurante(restauranteId: String): Result<Unit> {
        return try {
            // TODO: Adaptar quando a API estiver pronta para trabalhar com Firebase UID
            // Por enquanto, retornamos sucesso sem fazer nada
            // val response = candidaturaApiService.getCandidaturasByRestaurante(restauranteId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
