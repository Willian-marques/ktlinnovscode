package com.example.motoboyrecrutamento.ui.motoboy

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.motoboyrecrutamento.viewmodel.MotoboyViewModel

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
 * MEMBRO 3 - FASE 4: Tela de Perfil do Motoboy
 * 
 * RF07: Anexar Documentos
 * Permite que o Motoboy faça upload de documentos (CNH, comprovante de residência, etc.)
 * para seu perfil usando Firebase Storage.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoboyPerfilScreen(
    navController: NavController,
    motoboyId: Long = 0L, // ESTA LINHA DEVE ESTAR LÁ
    viewModel: MotoboyViewModel = viewModel()
) {
    val context = LocalContext.current
    var cnh by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var veiculo by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var raioAtuacao by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    
    val uploadState by viewModel.uploadState.collectAsState()
    val motoboyPerfil by viewModel.motoboyPerfil.collectAsState()
    val perfilSaveState by viewModel.perfilSaveState.collectAsState()
    
    // Carregar dados do perfil quando a tela abrir
    LaunchedEffect(Unit) {
        viewModel.loadPerfil()
    }
    
    // Preencher campos quando o perfil for carregado
    LaunchedEffect(motoboyPerfil) {
        motoboyPerfil?.let { perfil ->
            cnh = perfil.cnh
            telefone = perfil.telefone
            veiculo = perfil.veiculo
            experiencia = perfil.experienciaAnos.toString()
            raioAtuacao = perfil.raioAtuacao.toInt().toString()
        }
    }
    
    // Observar estado de salvamento
    LaunchedEffect(perfilSaveState) {
        when (perfilSaveState) {
            is MotoboyViewModel.PerfilSaveState.Success -> {
                Toast.makeText(context, "Alterações feitas com sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.resetPerfilSaveState()
                navController.popBackStack()
            }
            is MotoboyViewModel.PerfilSaveState.Error -> {
                Toast.makeText(
                    context,
                    (perfilSaveState as MotoboyViewModel.PerfilSaveState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetPerfilSaveState()
            }
            else -> {}
        }
    }
    
    // Launcher para seleção de arquivo
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informações Profissionais",
                style = MaterialTheme.typography.titleLarge
            )
            
            OutlinedTextField(
                value = telefone,
                onValueChange = { newValue ->
                    val digits = newValue.filter { it.isDigit() }
                    if (digits.length <= 11) {
                        telefone = formatPhoneNumber(digits)
                    }
                },
                label = { Text("Telefone (DDD + Número)") },
                placeholder = { Text("(11) 98765-4321") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            
            OutlinedTextField(
                value = cnh,
                onValueChange = { cnh = it },
                label = { Text("CNH") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = veiculo,
                onValueChange = { veiculo = it },
                label = { Text("Veículo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Ex: Moto Honda CG 160") }
            )
            
            OutlinedTextField(
                value = experiencia,
                onValueChange = { 
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        experiencia = it
                    }
                },
                label = { Text("Anos de Experiência") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            OutlinedTextField(
                value = raioAtuacao,
                onValueChange = { 
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        raioAtuacao = it
                    }
                },
                label = { Text("Raio de Atuação (km)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Ex: 10") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            Divider()
            
            Text(
                text = "Documentos",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(
                onClick = { filePickerLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selecionar Documento")
            }
            
            if (selectedFileUri != null) {
                Text(
                    text = "Arquivo selecionado: ${selectedFileUri?.lastPathSegment}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Button(
                    onClick = {
                        selectedFileUri?.let { uri ->
                            viewModel.uploadDocumento(uri, "CNH")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uploadState !is MotoboyViewModel.UploadState.Loading
                ) {
                    if (uploadState is MotoboyViewModel.UploadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Fazer Upload")
                    }
                }
            }
            
            // Exibir sucesso
            if (uploadState is MotoboyViewModel.UploadState.Success) {
                Text(
                    text = "Documento enviado com sucesso!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            // Exibir erro se houver
            if (uploadState is MotoboyViewModel.UploadState.Error) {
                Text(
                    text = (uploadState as MotoboyViewModel.UploadState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    // Validação básica
                    if (cnh.isNotBlank() && telefone.isNotBlank() && veiculo.isNotBlank() && 
                        experiencia.isNotBlank() && raioAtuacao.isNotBlank()) {
                        
                        val experienciaInt = experiencia.toIntOrNull() ?: 0
                        val raioDouble = raioAtuacao.toDoubleOrNull() ?: 0.0
                        
                        viewModel.salvarPerfil(
                            cnh = cnh,
                            telefone = telefone,
                            veiculo = veiculo,
                            experienciaAnos = experienciaInt,
                            raioAtuacao = raioDouble
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor, preencha todos os campos!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = perfilSaveState !is MotoboyViewModel.PerfilSaveState.Loading
            ) {
                if (perfilSaveState is MotoboyViewModel.PerfilSaveState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Salvar Perfil")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
