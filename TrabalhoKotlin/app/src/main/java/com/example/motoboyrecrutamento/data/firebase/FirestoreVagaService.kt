package com.example.motoboyrecrutamento.data.firebase

import android.util.Log
import com.example.motoboyrecrutamento.data.local.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Serviço para gerenciar vagas no Firebase Firestore
 * 
 * Estrutura do Firestore:
 * vagas/
 *   {vagaId}/
 *     - titulo: String
 *     - descricao: String
 *     - salario: Double
 *     - status: String ("aberta", "fechada")
 *     - requisitos: List<String>
 *     - dataPublicacao: String
 *     - restauranteId: String (Firebase UID)
 */
class FirestoreVagaService {
    private val firestore = FirebaseFirestore.getInstance()
    private val vagasCollection = firestore.collection("vagas")
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    /**
     * Criar uma nova vaga no Firestore
     */
    suspend fun createVaga(
        titulo: String,
        descricao: String,
        salario: Double,
        requisitos: List<String>,
        restauranteId: String
    ): Result<String> {
        return try {
            Log.d("FirestoreVaga", "Criando vaga: $titulo, restauranteId: $restauranteId")
            
            val vagaData = hashMapOf(
                "titulo" to titulo,
                "descricao" to descricao,
                "salario" to salario,
                "status" to "aberta",
                "requisitos" to requisitos,
                "dataPublicacao" to dateFormat.format(Date()),
                "restauranteId" to restauranteId
            )

            val docRef = vagasCollection.add(vagaData).await()
            Log.d("FirestoreVaga", "Vaga criada com sucesso! ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e("FirestoreVaga", "Erro ao criar vaga: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Buscar todas as vagas abertas (para motoboys)
     */
    suspend fun getVagasAbertas(): Result<List<Vaga>> {
        return try {
            Log.d("FirestoreVaga", "Buscando vagas abertas...")
            
            val snapshot = vagasCollection
                .whereEqualTo("status", "aberta")
                .get()
                .await()

            Log.d("FirestoreVaga", "Encontradas ${snapshot.documents.size} vagas no Firestore")

            val vagas = snapshot.documents.mapNotNull { doc ->
                try {
                    val vaga = Vaga(
                        id = doc.id.hashCode().toLong().let { if (it < 0) -it else it },
                        firestoreId = doc.id, // Salvar o ID do Firestore
                        titulo = doc.getString("titulo") ?: "",
                        descricao = doc.getString("descricao") ?: "",
                        salario = doc.getDouble("salario") ?: 0.0,
                        status = doc.getString("status") ?: "aberta",
                        requisitos = gson.toJson(doc.get("requisitos") ?: emptyList<String>()),
                        dataPublicacao = doc.getString("dataPublicacao") ?: "",
                        restauranteId = doc.getString("restauranteId") ?: ""
                    )
                    Log.d("FirestoreVaga", "Vaga processada: ${vaga.titulo}")
                    vaga
                } catch (e: Exception) {
                    Log.e("FirestoreVaga", "Erro ao processar vaga: ${e.message}")
                    null
                }
            }.sortedByDescending { it.dataPublicacao }

            Log.d("FirestoreVaga", "Total de vagas processadas: ${vagas.size}")
            Result.success(vagas)
        } catch (e: Exception) {
            Log.e("FirestoreVaga", "Erro ao buscar vagas: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Buscar vagas de um restaurante específico (para o dono)
     */
    suspend fun getVagasByRestaurante(restauranteId: String): Result<List<Vaga>> {
        return try {
            Log.d("FirestoreVaga", "Buscando vagas do restaurante: $restauranteId")
            
            val snapshot = vagasCollection
                .whereEqualTo("restauranteId", restauranteId)
                .get()
                .await()

            Log.d("FirestoreVaga", "Encontradas ${snapshot.documents.size} vagas do restaurante")

            val vagas = snapshot.documents.mapNotNull { doc ->
                try {
                    Vaga(
                        id = doc.id.hashCode().toLong().let { if (it < 0) -it else it },
                        firestoreId = doc.id, // Salvar o ID do Firestore
                        titulo = doc.getString("titulo") ?: "",
                        descricao = doc.getString("descricao") ?: "",
                        salario = doc.getDouble("salario") ?: 0.0,
                        status = doc.getString("status") ?: "aberta",
                        requisitos = gson.toJson(doc.get("requisitos") ?: emptyList<String>()),
                        dataPublicacao = doc.getString("dataPublicacao") ?: "",
                        restauranteId = doc.getString("restauranteId") ?: ""
                    )
                } catch (e: Exception) {
                    Log.e("FirestoreVaga", "Erro ao processar vaga do restaurante: ${e.message}")
                    null
                }
            }.sortedByDescending { it.dataPublicacao }

            Log.d("FirestoreVaga", "Total de vagas do restaurante processadas: ${vagas.size}")
            Result.success(vagas)
        } catch (e: Exception) {
            Log.e("FirestoreVaga", "Erro ao buscar vagas do restaurante: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Atualizar status de uma vaga
     */
    suspend fun updateVagaStatus(vagaId: String, status: String): Result<Unit> {
        return try {
            vagasCollection.document(vagaId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualizar uma vaga completa
     */
    suspend fun updateVaga(
        vagaId: String,
        titulo: String,
        descricao: String,
        salario: Double,
        requisitos: List<String>,
        status: String
    ): Result<Unit> {
        return try {
            val updates = hashMapOf(
                "titulo" to titulo,
                "descricao" to descricao,
                "salario" to salario,
                "requisitos" to requisitos,
                "status" to status
            )

            vagasCollection.document(vagaId)
                .update(updates as Map<String, Any>)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletar uma vaga permanentemente
     */
    suspend fun deleteVaga(vagaId: String): Result<Unit> {
        return try {
            vagasCollection.document(vagaId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Buscar uma vaga específica por ID
     */
    suspend fun getVagaById(vagaId: String): Result<Vaga> {
        return try {
            val doc = vagasCollection.document(vagaId).get().await()
            
            if (doc.exists()) {
                val vaga = Vaga(
                    id = doc.id.hashCode().toLong().let { if (it < 0) -it else it },
                    firestoreId = doc.id, // Salvar o ID do Firestore
                    titulo = doc.getString("titulo") ?: "",
                    descricao = doc.getString("descricao") ?: "",
                    salario = doc.getDouble("salario") ?: 0.0,
                    status = doc.getString("status") ?: "aberta",
                    requisitos = gson.toJson(doc.get("requisitos") ?: emptyList<String>()),
                    dataPublicacao = doc.getString("dataPublicacao") ?: "",
                    restauranteId = doc.getString("restauranteId") ?: ""
                )
                Result.success(vaga)
            } else {
                Result.failure(Exception("Vaga não encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
