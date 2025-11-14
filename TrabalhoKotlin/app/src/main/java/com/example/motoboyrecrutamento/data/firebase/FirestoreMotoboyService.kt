package com.example.motoboyrecrutamento.data.firebase

import android.util.Log
import com.example.motoboyrecrutamento.data.local.Motoboy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Serviço para gerenciar perfis de motoboys no Firestore
 */
class FirestoreMotoboyService {
    private val firestore = FirebaseFirestore.getInstance()
    private val motoboysCollection = firestore.collection("motoboys")

    /**
     * Salvar ou atualizar perfil do motoboy no Firestore
     */
    suspend fun saveMotoboy(
        motoboyId: String,
        nome: String,
        email: String,
        cnh: String,
        telefone: String,
        veiculo: String,
        experienciaAnos: Int,
        raioAtuacao: Double
    ): Result<String> {
        return try {
            Log.d("FirestoreMotoboy", "Salvando motoboy: $motoboyId")
            
            val motoboyData = hashMapOf(
                "nome" to nome,
                "email" to email,
                "cnh" to cnh,
                "telefone" to telefone,
                "veiculo" to veiculo,
                "experienciaAnos" to experienciaAnos,
                "raioAtuacao" to raioAtuacao,
                "updatedAt" to System.currentTimeMillis()
            )

            // Usar o Firebase UID como ID do documento
            motoboysCollection.document(motoboyId).set(motoboyData).await()
            
            Log.d("FirestoreMotoboy", "Motoboy salvo com sucesso!")
            Result.success(motoboyId)
        } catch (e: Exception) {
            Log.e("FirestoreMotoboy", "Erro ao salvar motoboy: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Buscar perfil do motoboy do Firestore
     */
    suspend fun getMotoboy(motoboyId: String): Result<Map<String, Any>?> {
        return try {
            Log.d("FirestoreMotoboy", "Buscando motoboy: $motoboyId")
            
            val snapshot = motoboysCollection.document(motoboyId).get().await()
            
            if (snapshot.exists()) {
                val data = snapshot.data
                Log.d("FirestoreMotoboy", "Motoboy encontrado: ${data?.get("nome")}")
                Result.success(data)
            } else {
                Log.d("FirestoreMotoboy", "Motoboy não encontrado no Firestore")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e("FirestoreMotoboy", "Erro ao buscar motoboy: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Deletar perfil do motoboy do Firestore
     */
    suspend fun deleteMotoboy(motoboyId: String): Result<Unit> {
        return try {
            Log.d("FirestoreMotoboy", "Deletando motoboy: $motoboyId")
            
            motoboysCollection.document(motoboyId).delete().await()
            
            Log.d("FirestoreMotoboy", "Motoboy deletado com sucesso")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreMotoboy", "Erro ao deletar motoboy: ${e.message}", e)
            Result.failure(e)
        }
    }
}
