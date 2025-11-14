package com.example.motoboyrecrutamento.ui.restaurante

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motoboyrecrutamento.viewmodel.RestauranteViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * MEMBRO 2 - FASE 3: Tela Principal do Restaurante
 *
 * Exibe a lista de vagas publicadas pelo restaurante.
 * Permite:
 * - Visualizar vagas publicadas
 * - Navegar para publicar nova vaga (RF04)
 * - Navegar para gerenciar candidaturas (RF06)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestauranteHomeScreen(
    navController: NavController,
    viewModel: RestauranteViewModel = viewModel()
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
                title = { Text("Minhas Vagas") },
                actions = {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("post_vaga") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Publicar Vaga")
            }
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
                Text("Nenhuma vaga publicada ainda.")
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
                                // Navegar para gerenciar candidaturas desta vaga
                                navController.navigate("gerenciar_candidaturas/${vaga.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Título e Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = vaga.titulo,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = if (vaga.status == "aberta")
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.errorContainer
                                ) {
                                    Text(
                                        text = vaga.status.uppercase(),
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = if (vaga.status == "aberta")
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }

                            // Descrição
                            Text(
                                text = vaga.descricao,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )

                            Divider()

                            // Informações em linha
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Salário",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "R$ ${String.format("%.2f", vaga.salario)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Publicada em",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = vaga.dataPublicacao,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            // Botões de Ação
                            if (vaga.status == "aberta") {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            navController.navigate("gerenciar_candidaturas/${vaga.id}")
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Ver Candidatos")
                                    }

                                    Button(
                                        onClick = { viewModel.encerrarVaga(vaga.id) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text("Encerrar")
                                    }
                                }
                            } else {
                                // Primeira linha - Ver Candidatos
                                OutlinedButton(
                                    onClick = {
                                        navController.navigate("gerenciar_candidaturas/${vaga.id}")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Ver Candidatos")
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Segunda linha - Reativar e Excluir
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.reativarVaga(vaga.id) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text("Reativar")
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.deleteVaga(vaga.id) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Excluir")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
