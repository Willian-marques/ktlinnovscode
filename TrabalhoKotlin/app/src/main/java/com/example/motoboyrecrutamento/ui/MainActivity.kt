package com.example.motoboyrecrutamento.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motoboyrecrutamento.ui.login.LoginScreen
import com.example.motoboyrecrutamento.ui.login.RegisterScreen
import com.example.motoboyrecrutamento.ui.login.ForgotPasswordScreen
import com.example.motoboyrecrutamento.ui.motoboy.MotoboyHomeScreen
import com.example.motoboyrecrutamento.ui.restaurante.RestauranteHomeScreen
import com.example.motoboyrecrutamento.ui.theme.MotoboyRecrutamentoTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.example.motoboyrecrutamento.ui.motoboy.MotoboyPerfilScreen
import com.example.motoboyrecrutamento.ui.motoboy.VagaDetalhesScreen
import com.example.motoboyrecrutamento.ui.restaurante.PostVagaScreen
import com.example.motoboyrecrutamento.ui.restaurante.GerenciarCandidaturasScreen
import com.example.motoboyrecrutamento.ui.restaurante.MotoboyDetalhesScreen


/**
 * MEMBRO 1 - FASE 1: Configuração e Autenticação
 *
 * Esta Activity é o ponto de entrada do aplicativo.
 * Responsável por:
 * - Inicializar o Firebase
 * - Configurar a navegação com Jetpack Compose Navigation
 * - Gerenciar o fluxo entre telas de autenticação e telas principais
 */


class MainActivity : ComponentActivity() {
    private fun getStartDestination(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            val userType = user.displayName ?: "motoboy"
            if (userType == "motoboy") "motoboy_home" else "restaurante_home"
        } else {
            "login"
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        setContent {
            MotoboyRecrutamentoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = getStartDestination()
                    ) {
                        // Telas de Autenticação (Membro 1)
                        composable("login") {
                            LoginScreen(navController = navController)
                        }
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(navController = navController)
                        }

                        // Tela Principal Motoboy (Membro 3)
                        composable("motoboy_home") {
                            MotoboyHomeScreen(navController = navController)
                        }

                        // Tela Principal Restaurante (Membro 2)
                        composable("restaurante_home") {
                            RestauranteHomeScreen(navController = navController)
                        }

                        // Tela de Publicar Vaga (Membro 2)
                        composable("post_vaga") {
                            PostVagaScreen(navController = navController)
                        }

                        // Tela de Detalhes da Vaga (Membro 3)
                        composable("vaga_detalhes/{vagaId}") { backStackEntry ->
                            val vagaId = backStackEntry.arguments?.getString("vagaId")?.toLongOrNull() ?: 0L
                            VagaDetalhesScreen(
                                navController = navController,
                                vagaId = vagaId
                            )
                        }

                        // Tela de Perfil do Motoboy (Membro 3)
                        composable("motoboy_perfil") {
                            MotoboyPerfilScreen(navController = navController)
                        }

                        // Tela de Detalhes do Motoboy (Usada pelo Restaurante)
                        composable("motoboy_detalhes/{motoboyId}") { backStackEntry ->
                            val motoboyId = backStackEntry.arguments?.getString("motoboyId")?.toLongOrNull() ?: 0L
                            MotoboyDetalhesScreen( // NOVO: Tela dedicada para detalhes do motoboy
                                navController = navController,
                                motoboyId = motoboyId
                            )
                        }

                        // Tela de Gerenciamento de Candidaturas (Membro 2)
                        composable("gerenciar_candidaturas/{vagaId}") { backStackEntry ->
                            val vagaId = backStackEntry.arguments?.getString("vagaId")?.toLongOrNull() ?: 0L
                            GerenciarCandidaturasScreen(
                                navController = navController,
                                vagaId = vagaId
                            )
                        }
                    }
                }
            }
        }
    }
}
