package com.example.motoboyrecrutamento.ui.restaurante

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
 * MEMBRO 2 - FASE 4: Tela de Gerenciamento de Candidaturas
 *
 * RF06: Gerenciar candidaturas
 * Permite que o Restaurante visualize as candidaturas para uma vaga específica.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenciarCandidaturasScreen(
    navController: NavController,
    vagaId: Long,
    viewModel: RestauranteViewModel = viewModel() // CORREÇÃO: Removido <Any?>
) {
    // NOVO: Observa a lista de candidaturas do ViewModel
    val candidaturas by viewModel.candidaturas.collectAsState()

    // NOVO: Carrega as candidaturas quando a tela é iniciada
    LaunchedEffect(vagaId) {
        viewModel.loadCandidaturas(vagaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candidaturas para Vaga $vagaId") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Funcionalidade de Gerenciamento de Candidaturas em desenvolvimento.")
            Text("Vaga ID: $vagaId")

            // NOVO: Lógica para exibir a lista de candidaturas
            if (candidaturas.isEmpty()) {
                Text("Nenhuma candidatura para esta vaga ainda.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(candidaturas) { candidatura ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Exibe o ID do Motoboy, Status e Data
                                Text(
                                    "Motoboy ID: ${candidatura.motoboyId}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.clickable {
                                        navController.navigate("motoboy_detalhes/${candidatura.motoboyId}")
                                    }
                                )
                                Text(
                                    "Status: ${candidatura.status}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "Data: ${candidatura.dataCandidatura}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
