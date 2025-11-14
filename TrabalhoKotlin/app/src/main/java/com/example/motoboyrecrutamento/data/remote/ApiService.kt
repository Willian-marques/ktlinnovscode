package com.example.motoboyrecrutamento.data.remote

import retrofit2.Response
import retrofit2.http.*

/**
 * FASE 2: Interfaces Retrofit para comunicação com a API REST
 *
 * IMPORTANTE: Estas interfaces definem os endpoints da API.
 * Você precisará substituir a BASE_URL pelo endereço real da sua API.
 *
 * Os DTOs (Data Transfer Objects  ) são classes simples que representam
 * os dados enviados/recebidos da API.
 */

/**
 * Data Transfer Objects (DTOs)
 */
data class VagaDTO(
    val id: Long?,
    val titulo: String,
    val descricao: String,
    val salario: Double,
    val status: String,
    val requisitos: List<String>,
    val dataPublicacao: String,
    val restauranteId: Long
)

data class CandidaturaDTO(
    val id: Long?,
    val vagaId: Long,
    val motoboyId: Long,
    val dataCandidatura: String,
    val status: String
)

data class PostVagaRequest(
    val titulo: String,
    val descricao: String,
    val salario: Double,
    val requisitos: List<String>,
    val restauranteId: Long
)

data class PostCandidaturaRequest(
    val vagaId: Long,
    val motoboyId: Long
)

/**
 * Interface Retrofit para endpoints de Vagas
 * MEMBRO 2: Usado principalmente pelo Restaurante
 */
interface VagaApiService {

    @GET("vagas")
    suspend fun getVagas(): Response<List<VagaDTO>>

    @GET("vagas/restaurante/{restauranteId}")
    suspend fun getVagasByRestaurante(@Path("restauranteId") restauranteId: Long): Response<List<VagaDTO>>

    @GET("vagas/{id}")
    suspend fun getVagaById(@Path("id") id: Long): Response<VagaDTO>

    @POST("vagas")
    suspend fun postVaga(@Body request: PostVagaRequest): Response<VagaDTO>

    @PUT("vagas/{id}")
    suspend fun updateVaga(
        @Path("id") id: Long,
        @Body vaga: VagaDTO
    ): Response<VagaDTO>

    @DELETE("vagas/{id}")
    suspend fun deleteVaga(@Path("id") id: Long): Response<Unit>
}

/**
 * Interface Retrofit para endpoints de Candidaturas
 * MEMBRO 3: Usado principalmente pelo Motoboy
 */
interface CandidaturaApiService {

    @GET("candidaturas/motoboy/{motoboyId}")
    suspend fun getCandidaturasByMotoboy(
        @Path("motoboyId") motoboyId: Long
    ): Response<List<CandidaturaDTO>>

    @GET("candidaturas/vaga/{vagaId}")
    suspend fun getCandidaturasByVaga(
        @Path("vagaId") vagaId: Long
    ): Response<List<CandidaturaDTO>>

    @GET("candidaturas/restaurante/{restauranteId}")
    suspend fun getCandidaturasByRestaurante(
        @Path("restauranteId") restauranteId: Long
    ): Response<List<CandidaturaDTO>>

    @POST("candidaturas")
    suspend fun postCandidatura(
        @Body request: PostCandidaturaRequest
    ): Response<CandidaturaDTO>

    @PUT("candidaturas/{id}")
    suspend fun updateCandidatura(
        @Path("id") id: Long,
        @Body candidatura: CandidaturaDTO
    ): Response<CandidaturaDTO>
}
