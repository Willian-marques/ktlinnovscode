package com.example.motoboyrecrutamento.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit

/**
 * FASE 2: Configuração do Retrofit
 *
 * Singleton que fornece instâncias configuradas do Retrofit
 * para comunicação com a API REST.
 *
 * IMPORTANTE: Substitua a BASE_URL pelo endereço real da sua API.
 * Exemplo: "https://api.exemplo.com/" ou "http://10.0.2.2:3000/" (para emulador Android )
 */
object RetrofitClient {

    // TODO: SUBSTITUIR PELA URL REAL DA API
    // Para testes, o MockApiService será usado automaticamente
    private const val BASE_URL = "https://api.exemplo.com/"

    // Flag para usar API mock (para testes sem backend real )
    private const val USE_MOCK_API = true

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // 1. Inicialização do Gson com o TypeAdapter para List<String>
    private val gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<List<String>>() {}.type, object : TypeAdapter<List<String>>() {
            override fun write(out: com.google.gson.stream.JsonWriter, value: List<String>?) {
                out.beginArray()
                value?.forEach { out.value(it) }
                out.endArray()
            }

            override fun read(input: com.google.gson.stream.JsonReader): List<String> {
                val list = mutableListOf<String>()
                input.beginArray()
                while (input.hasNext()) {
                    list.add(input.nextString())
                }
                input.endArray()
                return list
            }
        })
        .create()

    // 2. Inicialização do Retrofit (agora o 'gson' existe)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val vagaApiService: VagaApiService by lazy {
        if (USE_MOCK_API) {
            MockVagaApiService()
        } else {
            retrofit.create(VagaApiService::class.java)
        }
    }

    val candidaturaApiService: CandidaturaApiService by lazy {
        if (USE_MOCK_API) {
            MockCandidaturaApiService()
        } else {
            retrofit.create(CandidaturaApiService::class.java)
        }
    }
}
