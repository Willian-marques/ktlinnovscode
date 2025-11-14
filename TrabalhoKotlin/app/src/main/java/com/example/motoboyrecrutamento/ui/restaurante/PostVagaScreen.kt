package com.example.motoboyrecrutamento.ui.restaurante

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
import com.example.motoboyrecrutamento.viewmodel.RestauranteViewModel

/**
 * MEMBRO 2 - FASE 3: Tela de Publicar Vaga
 * 
 * RF04: Postar Vaga de Trabalho
 * Permite que o Restaurante crie e publique novas vagas de emprego para Motoboys.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostVagaScreen(
    navController: NavController,
    viewModel: RestauranteViewModel = viewModel()
) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var salario by remember { mutableStateOf("") }
    var requisitos by remember { mutableStateOf("") }
    val postVagaState by viewModel.postVagaState.collectAsState()
    
    // Navegar de volta quando a vaga for publicada com sucesso
    LaunchedEffect(postVagaState) {
        if (postVagaState is RestauranteViewModel.PostVagaState.Success) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Nova Vaga") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título da Vaga") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 6
            )
            
            OutlinedTextField(
                value = salario,
                onValueChange = { salario = it },
                label = { Text("Salário (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = requisitos,
                onValueChange = { requisitos = it },
                label = { Text("Requisitos (separados por vírgula)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                placeholder = { Text("Ex: CNH categoria A, Veículo próprio, 2 anos de experiência") }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val salarioDouble = salario.toDoubleOrNull() ?: 0.0
                    val requisitosList = requisitos.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    
                    viewModel.postVaga(
                        titulo = titulo,
                        descricao = descricao,
                        salario = salarioDouble,
                        requisitos = requisitosList
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = postVagaState !is RestauranteViewModel.PostVagaState.Loading &&
                        titulo.isNotEmpty() && descricao.isNotEmpty() && salario.isNotEmpty()
            ) {
                if (postVagaState is RestauranteViewModel.PostVagaState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Publicar Vaga")
                }
            }
            
            // Exibir erro se houver
            if (postVagaState is RestauranteViewModel.PostVagaState.Error) {
                Text(
                    text = (postVagaState as RestauranteViewModel.PostVagaState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
