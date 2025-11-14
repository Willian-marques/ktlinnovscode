package com.example.motoboyrecrutamento.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * FASE 2: Entidades do Banco de Dados Local (Room)
 *
 * Estas entidades representam as tabelas do banco de dados local
 * e são usadas para cache de dados sincronizados com a API.
 */

/**
 * Entidade base para todos os usuários
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firebaseUid: String, // UID do Firebase Authentication
    val nome: String,
    val email: String,
    val tipo: String, // "motoboy" ou "restaurante"
    val dataCriacao: String // LocalDateTime serializado como String
)

/**
 * MEMBRO 2: Entidade Restaurante
 * Armazena dados específicos do perfil Restaurante
 */
@Entity(tableName = "restaurantes")
data class Restaurante(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long, // Foreign key para Usuario
    val cnpj: String,
    val endereco: String,
    val telefone: String
)

/**
 * MEMBRO 3: Entidade Motoboy
 * Armazena dados específicos do perfil Motoboy
 */
@Entity(tableName = "motoboys")
data class Motoboy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long, // Foreign key para Usuario
    val cnh: String,
    val veiculo: String,
    val experienciaAnos: Int,
    val raioAtuacao: Double,
    val telefone: String
)

/**
 * MEMBRO 2: Entidade Vaga
 * Armazena o cache das vagas publicadas
 */
@Entity(tableName = "vagas")
data class Vaga(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firestoreId: String? = null, // ID do documento no Firestore
    val titulo: String,
    val descricao: String,
    val salario: Double,
    val status: String, // "aberta", "fechada"
    val requisitos: String, // Lista de requisitos serializada como String (JSON)
    val dataPublicacao: String, // LocalDateTime serializado
    val restauranteId: String // Firebase UID do restaurante
)

/**
 * MEMBRO 3: Entidade Candidatura
 * Armazena o cache das candidaturas do motoboy logado
 */
@Entity(tableName = "candidaturas")
data class Candidatura(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vagaId: Long, // Foreign key para Vaga
    val motoboyId: Long, // Foreign key para Motoboy
    val dataCandidatura: String, // LocalDateTime serializado
    val status: String, // "pendente", "aceita", "rejeitada"
    val firestoreId: String? = null, // ID do documento no Firestore
    val motoboyNome: String? = null, // Nome do motoboy (desnormalizado)
    val motoboyEmail: String? = null, // Email do motoboy (desnormalizado)
    val motoboyTelefone: String? = null // Telefone do motoboy (desnormalizado)
)
