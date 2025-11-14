package com.example.motoboyrecrutamento.ui.motoboy

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
import com.example.motoboyrecrutamento.viewmodel.MotoboyViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



/**
 * MEMBRO 3 - FASE 4: Tela de Detalhes da Vaga
 * 
 * RF05: Enviar Candidatura
 * Permite que o Motoboy visualize os detalhes de uma vaga e se candidate.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VagaDetalhesScreen(
    navController: NavController,
    vagaId: Long,
    viewModel: MotoboyViewModel = viewModel()
) {
    val vaga by viewModel.vagaSelecionada.collectAsState()
    val candidaturaState by viewModel.candidaturaState.collectAsState()
    val candidaturaExistente by viewModel.candidaturaExistente.collectAsState() // <-- NOVO ESTADO
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Carregar detalhes da vaga
    LaunchedEffect(vagaId) {
        viewModel.loadVagaDetalhes(vagaId)
    }
    
    // Navegar de volta quando a candidatura for enviada com sucesso
    LaunchedEffect(candidaturaState) {
        if (candidaturaState is MotoboyViewModel.CandidaturaState.Success) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Vaga") },
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
        if (vaga == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = vaga!!.titulo,
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Divider()
                
                Text(
                    text = "Descrição",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = vaga!!.descricao,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Divider()
                
                Text(
                    text = "Salário",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "R$ ${String.format("%.2f", vaga!!.salario)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Divider()
                
                Text(
                    text = "Requisitos",
                    style = MaterialTheme.typography.titleMedium
                )

                val requisitos = try {
                    val listType = object : TypeToken<List<String>>() {}.type // <-- ALTERADO
                    Gson().fromJson(vaga!!.requisitos, listType) as List<String> // <-- ALTERADO
                } catch (e: Exception) {
                    emptyList()
                }
                
                if (requisitos.isNotEmpty()) {
                    requisitos.forEach { requisito ->
                        Text(
                            text = "• $requisito",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    Text(
                        text = "Nenhum requisito especificado",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = { viewModel.enviarCandidatura(vagaId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = candidaturaState !is MotoboyViewModel.CandidaturaState.Loading && !candidaturaExistente
                ) {
                    if (candidaturaState is MotoboyViewModel.CandidaturaState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else if (candidaturaExistente) {
                        Text("Você já enviou a candidatura")
                    } else {
                        Text("Enviar Candidatura")
                    }
                }

                // Exibir erro se houver
                if (candidaturaState is MotoboyViewModel.CandidaturaState.Error && !(candidaturaState as MotoboyViewModel.CandidaturaState.Error).message.contains("Você já se candidatou")) { // <-- ALTERADO
                    Text(
                        text = (candidaturaState as MotoboyViewModel.CandidaturaState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
