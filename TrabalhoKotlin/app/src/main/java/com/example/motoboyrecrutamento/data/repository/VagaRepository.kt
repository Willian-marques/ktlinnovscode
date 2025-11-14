package com.example.motoboyrecrutamento.data.repository

import android.util.Log
import com.example.motoboyrecrutamento.data.local.Vaga
import com.example.motoboyrecrutamento.data.local.VagaDao
import com.example.motoboyrecrutamento.data.remote.*
import com.example.motoboyrecrutamento.data.firebase.FirestoreVagaService
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.util.NoSuchElementException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



/**
 * Repository para Vagas com integração híbrida:
 * - Room Database (cache local para modo offline)
 * - Firebase Firestore (dados online sincronizados)
 *
 * Arquitetura: Room ↔️ Repository ↔️ Firestore
 * - Room é a fonte de dados para a UI (offline-first)
 * - Firestore é a fonte de verdade (sincroniza quando online)
 */
class VagaRepository(
    private val vagaDao: VagaDao,
    private val vagaApiService: VagaApiService
) {
    private val firestoreService = FirestoreVagaService()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    /**
     * Retorna um Flow de vagas abertas do banco local (Room).
     * A UI observa este Flow para exibir dados reativos.
     */
    fun getVagasAbertas(): Flow<List<Vaga>> {
        return vagaDao.getVagasAbertas()
    }

    /**
     * Retorna um Flow de vagas de um restaurante específico.
     */
    fun getVagasByRestaurante(restauranteId: String): Flow<List<Vaga>> {
        return vagaDao.getVagasByRestaurante(restauranteId)
    }

    /**
     * Sincroniza vagas do Firestore com o banco local.
     * Deve ser chamado ao abrir o aplicativo ou fazer refresh.
     * 
     * Para Motoboy: busca todas as vagas abertas
     * Para Restaurante: busca apenas suas vagas
     * 
     * Faz merge inteligente: mantém mudanças locais quando houver conflito
     */
    suspend fun syncVagasFromFirestore(isRestaurante: Boolean = false): Result<Unit> {
        return try {
            val vagas = if (isRestaurante) {
                // Restaurante: buscar apenas suas vagas
                val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuário não autenticado"))
                firestoreService.getVagasByRestaurante(uid).getOrThrow()
            } else {
                // Motoboy: buscar todas as vagas abertas
                firestoreService.getVagasAbertas().getOrThrow()
            }

            // Fazer merge inteligente
            val localVagas = vagaDao.getAllVagasSync()
            val firestoreIds = vagas.mapNotNull { it.firestoreId }.toSet()
            
            // 1. Atualizar/inserir vagas do Firestore
            vagas.forEach { vagaFirestore ->
                val vagaLocal = vagaFirestore.firestoreId?.let { firestoreId ->
                    localVagas.find { it.firestoreId == firestoreId }
                }
                
                if (vagaLocal != null && vagaLocal.status == "encerrada") {
                    // Se a vaga foi encerrada localmente, manter o status local
                    android.util.Log.d("VagaRepository", "Vaga ${vagaLocal.id} foi encerrada localmente, mantendo status")
                    // Não atualizar para não perder a mudança local
                } else {
                    // Inserir ou atualizar normalmente
                    vagaDao.insert(vagaFirestore)
                }
            }
            
            // 2. Deletar vagas locais que não existem mais no Firestore
            // (mas apenas as que têm firestoreId - vagas antigas sem firestoreId são ignoradas)
            localVagas.forEach { vagaLocal ->
                if (vagaLocal.firestoreId != null && vagaLocal.firestoreId !in firestoreIds) {
                    android.util.Log.d("VagaRepository", "Deletando vaga local ${vagaLocal.id} que não existe mais no Firestore")
                    vagaDao.delete(vagaLocal)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            // Se falhar (sem internet), continua com dados do cache local
            Result.failure(e)
        }
    }

    /**
     * RF04: Publicar uma nova vaga (Restaurante)
     * 
     * 1. Salva no Firestore (fonte de verdade)
     * 2. Salva no Room (cache local)
     */
    suspend fun postVaga(
        titulo: String,
        descricao: String,
        salario: Double,
        requisitos: List<String>,
        restauranteId: String
    ): Result<Vaga> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuário não autenticado"))

            // 1. Criar no Firestore
            val firestoreId = firestoreService.createVaga(
                titulo = titulo,
                descricao = descricao,
                salario = salario,
                requisitos = requisitos,
                restauranteId = uid
            ).getOrThrow()

            // 2. Salvar no cache local
            val vaga = Vaga(
                id = firestoreId.hashCode().toLong().let { if (it < 0) -it else it },
                firestoreId = firestoreId, // Salvar o ID do Firestore
                titulo = titulo,
                descricao = descricao,
                salario = salario,
                status = "aberta",
                requisitos = Gson().toJson(requisitos),
                dataPublicacao = dateFormat.format(Date()),
                restauranteId = restauranteId
            )

            vagaDao.insert(vaga)

            Result.success(vaga)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualizar uma vaga existente
     * Verifica se o restaurante é o dono da vaga antes de atualizar
     * Sincroniza: Firestore + Room
     */
    suspend fun updateVaga(vaga: Vaga, restauranteId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuário não autenticado"))
            
            // Verificar se o restaurante é o dono da vaga
            if (vaga.restauranteId != restauranteId) {
                return Result.failure(Exception("Você não tem permissão para editar esta vaga"))
            }
            
            val requisitos = Gson().fromJson(vaga.requisitos, List::class.java) as List<String>

            // Atualizar no Firestore
            val vagaIdString = vaga.id.toString()
            firestoreService.updateVaga(
                vagaId = vagaIdString,
                titulo = vaga.titulo,
                descricao = vaga.descricao,
                salario = vaga.salario,
                requisitos = requisitos,
                status = vaga.status
            ).getOrThrow()

            // Atualizar no Room
            vagaDao.update(vaga)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletar uma vaga permanentemente
     * Sincroniza: Firestore + Room
     */
    suspend fun deleteVaga(vagaId: Long): Result<Unit> {
        return try {
            Log.d("VagaRepository", "deleteVaga: vagaId=$vagaId")
            
            // 1. Buscar a vaga no banco local
            val vaga = vagaDao.getVagaByIdSync(vagaId) 
                ?: throw NoSuchElementException("Vaga não encontrada no banco de dados local.")

            Log.d("VagaRepository", "Vaga encontrada: id=${vaga.id}, firestoreId=${vaga.firestoreId}")

            // 2. Deletar no Firestore (usar firestoreId se disponível)
            if (vaga.firestoreId != null) {
                Log.d("VagaRepository", "Deletando do Firestore com firestoreId=${vaga.firestoreId}")
                firestoreService.deleteVaga(vaga.firestoreId).getOrThrow()
            } else {
                Log.w("VagaRepository", "Vaga sem firestoreId, tentando com vagaId como fallback")
                firestoreService.deleteVaga(vagaId.toString()).getOrThrow()
            }

            // 3. Deletar no Room
            vagaDao.delete(vaga)
            Log.d("VagaRepository", "Vaga deletada com sucesso do Room")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("VagaRepository", "Erro ao deletar vaga: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * RF05: Encerrar/Reativar uma vaga (Restaurante)
     * Verifica se o restaurante é o dono da vaga antes de alterar o status
     * Sincroniza: Firestore + Room
     */
    suspend fun updateVagaStatus(vagaId: Long, status: String, restauranteId: String): Result<Unit> {
        return try {
            android.util.Log.d("VagaRepository", "updateVagaStatus: vagaId=$vagaId, status=$status, restauranteId=$restauranteId")
            
            // 1. Buscar a vaga atual no banco local
            val vaga = vagaDao.getVagaByIdSync(vagaId) 
                ?: throw NoSuchElementException("Vaga não encontrada no banco de dados local.")

            android.util.Log.d("VagaRepository", "Vaga encontrada: id=${vaga.id}, firestoreId=${vaga.firestoreId}, restauranteId=${vaga.restauranteId}")

            // 2. Verificar se o restaurante é o dono da vaga
            if (vaga.restauranteId != restauranteId) {
                android.util.Log.e("VagaRepository", "Permissão negada: vaga.restauranteId=${vaga.restauranteId}, restauranteId=$restauranteId")
                return Result.failure(Exception("Você não tem permissão para modificar esta vaga"))
            }

            // 3. Atualizar status no Firestore (usar firestoreId se disponível)
            val firestoreDocId = vaga.firestoreId ?: vagaId.toString()
            android.util.Log.d("VagaRepository", "Atualizando Firestore com firestoreDocId=$firestoreDocId")
            firestoreService.updateVagaStatus(firestoreDocId, status).getOrThrow()

            // 4. Atualizar no Room
            val vagaAtualizada = vaga.copy(status = status)
            vagaDao.update(vagaAtualizada)
            android.util.Log.d("VagaRepository", "Status atualizado com sucesso no Room")

            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("VagaRepository", "Erro ao atualizar status: ${e.message}", e)
            Result.failure(e)
        }
    }
}