package com.example.motoboyrecrutamento.data.remote

import retrofit2.Response

/**
 * Implementação Mock do VagaApiService
 * Usa MockApiService internamente para simular chamadas de API
 */
class MockVagaApiService : VagaApiService {
    
    override suspend fun getVagas(): Response<List<VagaDTO>> {
        return MockApiService.getVagas()
    }
    
    override suspend fun getVagaById(id: Long): Response<VagaDTO> {
        return MockApiService.getVagaById(id)
    }
    
    override suspend fun postVaga(request: PostVagaRequest): Response<VagaDTO> {
        return MockApiService.postVaga(request)
    }
    
    override suspend fun updateVaga(id: Long, vaga: VagaDTO): Response<VagaDTO> {
        return MockApiService.updateVaga(id, vaga)
    }
    
    override suspend fun deleteVaga(id: Long): Response<Unit> {
        return MockApiService.deleteVaga(id)
    }

    override suspend fun getVagasByRestaurante(restauranteId: Long): Response<List<VagaDTO>> {
        return MockApiService.getVagasByRestaurante(restauranteId)
    }
}

/**
 * Implementação Mock do CandidaturaApiService
 * Usa MockApiService internamente para simular chamadas de API
 */
class MockCandidaturaApiService : CandidaturaApiService {
    
    override suspend fun getCandidaturasByMotoboy(motoboyId: Long): Response<List<CandidaturaDTO>> {
        return MockApiService.getCandidaturasByMotoboy(motoboyId)
    }
    
    override suspend fun getCandidaturasByVaga(vagaId: Long): Response<List<CandidaturaDTO>> {
        return MockApiService.getCandidaturasByVaga(vagaId)
    }
    
    override suspend fun postCandidatura(request: PostCandidaturaRequest): Response<CandidaturaDTO> {
        return MockApiService.postCandidatura(request)
    }
    
    override suspend fun updateCandidatura(id: Long, candidatura: CandidaturaDTO): Response<CandidaturaDTO> {
        return MockApiService.updateCandidatura(id, candidatura)
    }
    override suspend fun getCandidaturasByRestaurante(restauranteId: Long): Response<List<CandidaturaDTO>> {
        return MockApiService.getCandidaturasByRestaurante(restauranteId)
    }
}
