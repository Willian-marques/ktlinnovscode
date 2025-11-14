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
 * NOVO: Tela de Detalhes do Motoboy para o Restaurante
 *
 * Exibe o perfil detalhado de um motoboy que se candidatou a uma vaga.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoboyDetalhesScreen(
    navController: NavController,
    motoboyId: Long,
    viewModel: RestauranteViewModel = viewModel()
) {
    // O RestauranteViewModel precisa de uma função para carregar os detalhes do Motoboy
    // Vamos usar o MotoboyViewModel para carregar o perfil do motoboy
    // Para simplificar, vamos usar o MotoboyViewModel, mas ele precisa ser adaptado para carregar por ID.
    // Como não temos um MotoboyRepository, vamos simular o carregamento do perfil.

    // Para o escopo desta tarefa, vamos apenas exibir o ID e o telefone (que está no Entitie.kt)
    // O MotoboyViewModel já tem acesso ao MotoboyDao. Vamos criar uma função no RestauranteViewModel
    // para buscar o Motoboy por ID.

    // NOTA: O MotoboyDao já foi atualizado para ter getMotoboyByIdSync(motoboyId: Long)

    val motoboyDetalhes by viewModel.motoboyDetalhes.collectAsState()

    LaunchedEffect(motoboyId) {
        viewModel.loadMotoboyDetalhes(motoboyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Motoboy") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (motoboyDetalhes == null) {
                // Mostrar loading por 3 segundos, depois mostrar erro
                var showError by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000)
                    showError = true
                }
                
                if (!showError) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Carregando informações do motoboy...")
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Informações não disponíveis",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "O perfil deste motoboy ainda não foi completado.\nID do candidato: $motoboyId",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Voltar")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Informações do Motoboy",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            DetailRow("ID", motoboyDetalhes!!.id.toString())
                            DetailRow("CNH", motoboyDetalhes!!.cnh)
                            DetailRow("Veículo", motoboyDetalhes!!.veiculo)
                            DetailRow("Experiência", "${motoboyDetalhes!!.experienciaAnos} anos")
                            DetailRow("Raio de Atuação", "${motoboyDetalhes!!.raioAtuacao} km")
                            
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            
                            Text(
                                text = "Contato",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = motoboyDetalhes!!.telefone,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
