package com.example.motoboyrecrutamento.ui.motoboy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motoboyrecrutamento.viewmodel.MotoboyViewModel
import com.google.firebase.auth.FirebaseAuth



/**
 * MEMBRO 3 - FASE 4: Tela Principal do Motoboy
 * 
 * Exibe a lista de vagas disponíveis.
 * Permite:
 * - Visualizar vagas abertas
 * - Navegar para detalhes da vaga e enviar candidatura (RF05)
 * - Acessar perfil e documentos (RF07)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoboyHomeScreen(
    navController: NavController,
    viewModel: MotoboyViewModel = viewModel()
) {
    val vagas by viewModel.vagas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Carregar vagas ao abrir a tela
    LaunchedEffect(Unit) {
        viewModel.loadVagas()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vagas Disponíveis") },
                actions = {
                    TextButton(onClick = {
                        navController.navigate("motoboy_perfil")
                    }) {
                        Text("Perfil")
                    }
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Text("Sair")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (vagas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma vaga disponível no momento.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vagas) { vaga ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Navegar para detalhes da vaga
                                navController.navigate("vaga_detalhes/${vaga.id}")
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = vaga.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = vaga.descricao,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Salário: R$ ${String.format("%.2f", vaga.salario)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
