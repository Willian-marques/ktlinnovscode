package com.example.motoboyrecrutamento

import android.app.Application
import com.example.motoboyrecrutamento.data.remote.MockApiService

class MotoboyRecrutamentoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializa o MockApiService com contexto para persistência
        MockApiService.init(this)
    }
}