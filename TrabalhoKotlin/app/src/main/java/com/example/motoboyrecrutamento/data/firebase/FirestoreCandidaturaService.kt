package com.example.motoboyrecrutamento.data.firebase

import android.util.Log
import com.example.motoboyrecrutamento.data.local.Candidatura
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Serviço para gerenciar candidaturas no Firestore
 */
class FirestoreCandidaturaService {
    private val firestore = FirebaseFirestore.getInstance()
    private val candidaturasCollection = firestore.collection("candidaturas")

    /**
     * Criar uma nova candidatura no Firestore
     */
    suspend fun createCandidatura(
        vagaId: String,
        motoboyId: String,
        motoboyNome: String,
        motoboyEmail: String,
        motoboyTelefone: String
    ): Result<String> {
        return try {
            Log.d("FirestoreCandidatura", "Criando candidatura: vagaId=$vagaId, motoboyId=$motoboyId")
            
            val candidaturaData = hashMapOf(
                "vagaId" to vagaId,
                "motoboyId" to motoboyId,
                "motoboyNome" to motoboyNome,
                "motoboyEmail" to motoboyEmail,
                "motoboyTelefone" to motoboyTelefone,
                "status" to "pendente",
                "dataCandidatura" to com.google.firebase.Timestamp.now()
            )

            val docRef = candidaturasCollection.add(candidaturaData).await()
            val candidaturaId = docRef.id
            
            Log.d("FirestoreCandidatura", "Candidatura criada com sucesso! ID: $candidaturaId")
            Result.success(candidaturaId)
        } catch (e: Exception) {
            Log.e("FirestoreCandidatura", "Erro ao criar candidatura: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Buscar candidaturas de uma vaga específica
     */
    suspend fun getCandidaturasByVaga(vagaId: String): Result<List<Candidatura>> {
        return try {
            Log.d("FirestoreCandidatura", "Buscando candidaturas da vaga: $vagaId")
            
            val snapshot = candidaturasCollection
                .whereEqualTo("vagaId", vagaId)
                .get()
                .await()

            val candidaturas = snapshot.documents.mapNotNull { doc ->
                try {
                    Candidatura(
                        id = doc.id.hashCode().toLong().let { if (it < 0) -it else it },
                        vagaId = doc.getString("vagaId")?.hashCode()?.toLong()?.let { if (it < 0) -it else it } ?: 0,
                        motoboyId = doc.getString("motoboyId")?.hashCode()?.toLong()?.let { if (it < 0) -it else it } ?: 0,
                        dataCandidatura = doc.getTimestamp("dataCandidatura")?.toDate()?.let { 
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(it) 
                        } ?: "",
                        status = doc.getString("status") ?: "pendente",
                        firestoreId = doc.id,
                        motoboyNome = doc.getString("motoboyNome"),
                        motoboyEmail = doc.getString("motoboyEmail"),
                        motoboyTelefone = doc.getString("motoboyTelefone")
                    )
                } catch (e: Exception) {
                    Log.e("FirestoreCandidatura", "Erro ao parsear candidatura: ${e.message}")
                    null
                }
            }

            Log.d("FirestoreCandidatura", "Encontradas ${candidaturas.size} candidaturas")
            Result.success(candidaturas)
        } catch (e: Exception) {
            Log.e("FirestoreCandidatura", "Erro ao buscar candidaturas: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Atualizar status de uma candidatura
     */
    suspend fun updateCandidaturaStatus(candidaturaId: String, status: String): Result<Unit> {
        return try {
            Log.d("FirestoreCandidatura", "Atualizando status da candidatura $candidaturaId para $status")
            
            candidaturasCollection.document(candidaturaId)
                .update("status", status)
                .await()
            
            Log.d("FirestoreCandidatura", "Status atualizado com sucesso")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreCandidatura", "Erro ao atualizar status: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Deletar uma candidatura
     */
    suspend fun deleteCandidatura(candidaturaId: String): Result<Unit> {
        return try {
            Log.d("FirestoreCandidatura", "Deletando candidatura: $candidaturaId")
            
            candidaturasCollection.document(candidaturaId).delete().await()
            
            Log.d("FirestoreCandidatura", "Candidatura deletada com sucesso")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreCandidatura", "Erro ao deletar candidatura: ${e.message}", e)
            Result.failure(e)
        }
    }
}
