package com.example.motoboyrecrutamento.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motoboyrecrutamento.viewmodel.LoginViewModel

/**
 * MEMBRO 1 - FASE 1: Tela de Recuperação de Senha
 * 
 * RF03: Recuperar Senha
 * Permite que o usuário redefina sua senha em caso de esquecimento
 * usando Firebase Authentication (envio de e-mail de recuperação).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val resetPasswordState by viewModel.resetPasswordState.collectAsState()
    
    // Observar o estado e navegar de volta ao login quando bem-sucedido
    LaunchedEffect(resetPasswordState) {
        if (resetPasswordState is LoginViewModel.ResetPasswordState.Success) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Senha") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Digite seu e-mail para receber as instruções de recuperação de senha.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { viewModel.resetPassword(email) },
                modifier = Modifier.fillMaxWidth(),
                enabled = resetPasswordState !is LoginViewModel.ResetPasswordState.Loading
            ) {
                if (resetPasswordState is LoginViewModel.ResetPasswordState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enviar E-mail de Recuperação")
                }
            }
            
            // Exibir mensagem de sucesso
            if (resetPasswordState is LoginViewModel.ResetPasswordState.Success) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "E-mail de recuperação enviado com sucesso! Verifique sua caixa de entrada.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Exibir erro se houver
            if (resetPasswordState is LoginViewModel.ResetPasswordState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (resetPasswordState as LoginViewModel.ResetPasswordState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
