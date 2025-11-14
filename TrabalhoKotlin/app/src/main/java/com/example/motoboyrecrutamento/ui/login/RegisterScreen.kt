package com.example.motoboyrecrutamento.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motoboyrecrutamento.viewmodel.LoginViewModel

/**
 * Formata o telefone no padrão (XX) XXXXX-XXXX
 */
fun formatPhoneNumber(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    return when {
        digits.length <= 2 -> digits
        digits.length <= 7 -> "(${digits.substring(0, 2)}) ${digits.substring(2)}"
        digits.length <= 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
        else -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
    }
}

/**
 * Remove formatação do telefone
 */
fun unformatPhoneNumber(phone: String): String {
    return phone.filter { it.isDigit() }
}

/**
 * MEMBRO 1 - FASE 1: Tela de Cadastro
 * 
 * RF01: Cadastrar Usuário
 * Permite que novos usuários (Restaurantes ou Motoboys) se cadastrem na plataforma
 * usando Firebase Authentication.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedProfile by remember { mutableStateOf("motoboy") } // "motoboy" ou "restaurante"
    var telefone by remember { mutableStateOf("") } // NOVO: Campo de telefone
    val registerState by viewModel.registerState.collectAsState()
    
    // Observar o estado de registro e navegar quando bem-sucedido
    LaunchedEffect(registerState) {
        if (registerState is LoginViewModel.RegisterState.Success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Conta") },
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
            
            // NOVO: Campo de Telefone para Motoboy
            if (selectedProfile == "motoboy") {
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { newValue ->
                        // Apenas permitir dígitos e limitar a 11
                        val digits = newValue.filter { it.isDigit() }
                        if (digits.length <= 11) {
                            telefone = digits
                        }
                    },
                    label = { Text("Telefone (DDD + Número)") },
                    placeholder = { Text("11987654321") },
                    supportingText = { Text("Digite apenas números (ex: 11987654321)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Selecione seu perfil:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = selectedProfile == "motoboy",
                            onClick = { selectedProfile = "motoboy" }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedProfile == "motoboy",
                        onClick = { selectedProfile = "motoboy" }
                    )
                    Text("Sou Motoboy", modifier = Modifier.padding(start = 8.dp))
                }
                
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = selectedProfile == "restaurante",
                            onClick = { selectedProfile = "restaurante" }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedProfile == "restaurante",
                        onClick = { selectedProfile = "restaurante" }
                    )
                    Text("Sou Restaurante", modifier = Modifier.padding(start = 8.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        val tel = if (selectedProfile == "motoboy") telefone else null
                        viewModel.register(name, email, password, selectedProfile, tel)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is LoginViewModel.RegisterState.Loading
            ) {
                if (registerState is LoginViewModel.RegisterState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Cadastrar")
                }
            }
            
            // Exibir erro se houver
            if (registerState is LoginViewModel.RegisterState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (registerState as LoginViewModel.RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Validação de senha
            if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "As senhas não coincidem",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
