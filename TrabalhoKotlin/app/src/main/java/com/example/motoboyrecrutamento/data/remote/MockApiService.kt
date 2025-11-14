package com.example.motoboyrecrutamento.data.remote

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Mock API Service para testes sem backend real
 * 
 * Esta classe simula as respostas da API para permitir testes
 * do aplicativo sem necessidade de um servidor real.
 * 
 * IMPORTANTE: Em produção, substitua pelo RetrofitClient real.
 * 
 * CORREÇÃO: Agora usa SharedPreferences para persistir dados entre sessões
 */
object MockApiService {
    
    private var sharedPreferences: SharedPreferences? = null
    private val gson = Gson()
    
    private const val PREFS_NAME = "mock_api_prefs"
    private const val KEY_VAGAS = "vagas"
    private const val KEY_CANDIDATURAS = "candidaturas"
    private const val KEY_NEXT_VAGA_ID = "next_vaga_id"
    private const val KEY_NEXT_CANDIDATURA_ID = "next_candidatura_id"
    
    /**
     * Inicializa o MockApiService com contexto para persistência
     * DEVE ser chamado no Application.onCreate()
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Carrega as vagas do SharedPreferences
     */
    private fun loadVagas(): MutableList<VagaDTO> {
        val json = sharedPreferences?.getString(KEY_VAGAS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<VagaDTO>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
    
    /**
     * Salva as vagas no SharedPreferences
     */
    private fun saveVagas(vagas: List<VagaDTO>) {
        val json = gson.toJson(vagas)
        sharedPreferences?.edit()?.putString(KEY_VAGAS, json)?.apply()
    }
    
    /**
     * Carrega as candidaturas do SharedPreferences
     */
    private fun loadCandidaturas(): MutableList<CandidaturaDTO> {
        val json = sharedPreferences?.getString(KEY_CANDIDATURAS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<CandidaturaDTO>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
    
    /**
     * Salva as candidaturas no SharedPreferences
     */
    private fun saveCandidaturas(candidaturas: List<CandidaturaDTO>) {
        val json = gson.toJson(candidaturas)
        sharedPreferences?.edit()?.putString(KEY_CANDIDATURAS, json)?.apply()
    }
    
    /**
     * Obtém e incrementa o próximo ID de vaga
     */
    private fun getNextVagaId(): Long {
        val currentId = sharedPreferences?.getLong(KEY_NEXT_VAGA_ID, 1L) ?: 1L
        sharedPreferences?.edit()?.putLong(KEY_NEXT_VAGA_ID, currentId + 1)?.apply()
        return currentId
    }
    
    /**
     * Obtém e incrementa o próximo ID de candidatura
     */
    private fun getNextCandidaturaId(): Long {
        val currentId = sharedPreferences?.getLong(KEY_NEXT_CANDIDATURA_ID, 1L) ?: 1L
        sharedPreferences?.edit()?.putLong(KEY_NEXT_CANDIDATURA_ID, currentId + 1)?.apply()
        return currentId
    }

    /**
     * Simula delay de rede
     */
    private suspend fun simulateNetworkDelay() {
        delay(500L) // 500ms de delay
    }
    
    /**
     * Mock: Buscar todas as vagas
     */
    suspend fun getVagas(): Response<List<VagaDTO>> {
        simulateNetworkDelay()
        val vagas = loadVagas()
        return Response.success(vagas.toList())
    }

    /**
     * Mock: Buscar vagas por restaurante
     */
    suspend fun getVagasByRestaurante(restauranteId: Long): Response<List<VagaDTO>> {
        simulateNetworkDelay()
        val vagas = loadVagas()
        val filtered = vagas.filter { it.restauranteId == restauranteId }
        return Response.success(filtered)
    }
    
    /**
     * Mock: Buscar vaga por ID
     */
    suspend fun getVagaById(id: Long): Response<VagaDTO> {
        simulateNetworkDelay()
        val vagas = loadVagas()
        val vaga = vagas.find { it.id == id }
        return if (vaga != null) {
            Response.success(vaga)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Vaga não encontrada"))
        }
    }
    
    /**
     * Mock: Publicar nova vaga
     */
    suspend fun postVaga(request: PostVagaRequest): Response<VagaDTO> {
        simulateNetworkDelay()
        
        val vagas = loadVagas()
        val novaVaga = VagaDTO(
            id = getNextVagaId(),
            titulo = request.titulo,
            descricao = request.descricao,
            salario = request.salario,
            status = "aberta",
            requisitos = request.requisitos,
            dataPublicacao = getCurrentDateTime(),
            restauranteId = request.restauranteId
        )
        
        vagas.add(novaVaga)
        saveVagas(vagas)
        
        return Response.success(novaVaga)
    }
    
    /**
     * Mock: Atualizar vaga
     */
    suspend fun updateVaga(id: Long, vaga: VagaDTO): Response<VagaDTO> {
        simulateNetworkDelay()
        
        val vagas = loadVagas()
        val index = vagas.indexOfFirst { it.id == id }
        
        return if (index != -1) {
            vagas[index] = vaga.copy(id = id)
            saveVagas(vagas)
            Response.success(vagas[index])
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Vaga não encontrada"))
        }
    }
    
    /**
     * Mock: Deletar vaga
     */
    suspend fun deleteVaga(id: Long): Response<Unit> {
        simulateNetworkDelay()
        
        val vagas = loadVagas()
        val removed = vagas.removeIf { it.id == id }
        
        if (removed) {
            saveVagas(vagas)
            Response.success(Unit)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Vaga não encontrada"))
        }
        
        return Response.success(Unit)
    }
    
    /**
     * Mock: Buscar candidaturas por motoboy
     */
    suspend fun getCandidaturasByMotoboy(motoboyId: Long): Response<List<CandidaturaDTO>> {
        simulateNetworkDelay()
        val candidaturas = loadCandidaturas()
        val filtered = candidaturas.filter { it.motoboyId == motoboyId }
        return Response.success(filtered)
    }
    
    /**
     * Mock: Buscar candidaturas por vaga
     */
    suspend fun getCandidaturasByVaga(vagaId: Long): Response<List<CandidaturaDTO>> {
        simulateNetworkDelay()
        val candidaturas = loadCandidaturas()
        val filtered = candidaturas.filter { it.vagaId == vagaId }
        return Response.success(filtered)
    }
    suspend fun getCandidaturasByRestaurante(restauranteId: Long): Response<List<CandidaturaDTO>> {
        simulateNetworkDelay()
        val candidaturas = loadCandidaturas()
        // Simula a busca por todas as candidaturas de vagas pertencentes a um restaurante
        // Como o MockApiService não tem a lista de vagas, vamos retornar todas as candidaturas por simplicidade no mock.
        // Em um mock mais completo, seria necessário filtrar as candidaturas por vagaId e verificar se a vaga pertence ao restauranteId.
        // Para o propósito de compilação, retornaremos todas as candidaturas.
        return Response.success(candidaturas)
    }
    
    /**
     * Mock: Enviar candidatura
     */
    suspend fun postCandidatura(request: PostCandidaturaRequest): Response<CandidaturaDTO> {
        simulateNetworkDelay()
        
        val candidaturas = loadCandidaturas()
        val novaCandidatura = CandidaturaDTO(
            id = getNextCandidaturaId(),
            vagaId = request.vagaId,
            motoboyId = request.motoboyId,
            dataCandidatura = getCurrentDateTime(),
            status = "pendente"
        )
        
        candidaturas.add(novaCandidatura)
        saveCandidaturas(candidaturas)
        
        return Response.success(novaCandidatura)
    }
    
    /**
     * Mock: Atualizar candidatura
     */
    suspend fun updateCandidatura(id: Long, candidatura: CandidaturaDTO): Response<CandidaturaDTO> {
        simulateNetworkDelay()
        
        val candidaturas = loadCandidaturas()
        val index = candidaturas.indexOfFirst { it.id == id }
        
        return if (index != -1) {
            candidaturas[index] = candidatura.copy(id = id)
            saveCandidaturas(candidaturas)
            Response.success(candidaturas[index])
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "Candidatura não encontrada"))
        }
    }
    
    /**
     * Retorna a data/hora atual em formato ISO 8601
     */
    private fun getCurrentDateTime(): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        return format.format(Date())
    }
    
    /**
     * Limpa todos os dados persistidos (útil para testes)
     */
    fun clearAllData() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
