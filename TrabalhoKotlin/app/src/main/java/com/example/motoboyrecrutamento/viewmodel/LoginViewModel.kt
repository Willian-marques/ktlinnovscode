package com.example.motoboyrecrutamento.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * MEMBRO 1 - FASE 1: ViewModel de Autenticação
 * 
 * Gerencia a lógica de autenticação usando Firebase Authentication:
 * - RF01: Cadastro de usuário
 * - RF02: Login de usuário
 * - RF03: Recuperação de senha
 * 
 * IMPORTANTE: O tipo de usuário (motoboy/restaurante) é armazenado no displayName
 * temporariamente. Na Fase 2, isso será movido para o banco de dados Room.
 */
class LoginViewModel : ViewModel() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    // Estados de Login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    
    // Estados de Registro
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState
    
    // Estados de Recuperação de Senha
    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState
    
    /**
     * RF02: Login com Firebase Authentication
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                
                if (user != null) {
                    // Recuperar o tipo de usuário do displayName (temporário)
                    val userType = user.displayName ?: "motoboy"
                    _loginState.value = LoginState.Success(userType)
                } else {
                    _loginState.value = LoginState.Error("Erro ao fazer login")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("no user record", ignoreCase = true) == true ||
                    e.message?.contains("user not found", ignoreCase = true) == true -> 
                        "Usuário não encontrado. Verifique o e-mail digitado."
                    e.message?.contains("wrong password", ignoreCase = true) == true ||
                    e.message?.contains("invalid password", ignoreCase = true) == true ||
                    e.message?.contains("invalid credential", ignoreCase = true) == true -> 
                        "Senha incorreta. Tente novamente."
                    e.message?.contains("invalid email", ignoreCase = true) == true -> 
                        "E-mail inválido. Verifique o formato do e-mail."
                    e.message?.contains("network", ignoreCase = true) == true -> 
                        "Erro de conexão. Verifique sua internet."
                    e.message?.contains("too many requests", ignoreCase = true) == true -> 
                        "Muitas tentativas. Tente novamente mais tarde."
                    else -> "Erro ao fazer login: ${e.message ?: "Erro desconhecido"}"
                }
                _loginState.value = LoginState.Error(errorMessage)
            }
        }
    }
    
    /**
     * RF01: Cadastro com Firebase Authentication
     * 
     * @param userType "motoboy" ou "restaurante"
     */
    fun register(name: String, email: String, password: String, userType: String, telefone: String? = null) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                
                if (user != null) {
                    // Atualizar o perfil do usuário com o nome e tipo (temporário no displayName)
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(userType) // Armazenar tipo temporariamente
                        .setPhotoUri(if (telefone != null) android.net.Uri.parse(telefone) else null) // Armazenar telefone temporariamente na photoUri
                        .build()
                    
                    user.updateProfile(profileUpdates).await()
                    
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error("Erro ao criar conta")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("email address is already", ignoreCase = true) == true ||
                    e.message?.contains("email-already-in-use", ignoreCase = true) == true -> 
                        "Este e-mail já está cadastrado. Tente fazer login."
                    e.message?.contains("weak password", ignoreCase = true) == true -> 
                        "Senha muito fraca. Use no mínimo 6 caracteres."
                    e.message?.contains("invalid email", ignoreCase = true) == true -> 
                        "E-mail inválido. Verifique o formato do e-mail."
                    e.message?.contains("network", ignoreCase = true) == true -> 
                        "Erro de conexão. Verifique sua internet."
                    else -> "Erro ao criar conta: ${e.message ?: "Erro desconhecido"}"
                }
                _registerState.value = RegisterState.Error(errorMessage)
            }
        }
    }
    
    /**
     * RF03: Recuperação de Senha com Firebase Authentication
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                _resetPasswordState.value = ResetPasswordState.Loading
                
                // Validar formato do email antes
                if (email.isBlank()) {
                    _resetPasswordState.value = ResetPasswordState.Error("Digite um e-mail válido")
                    return@launch
                }
                
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    _resetPasswordState.value = ResetPasswordState.Error("Formato de e-mail inválido")
                    return@launch
                }
                
                // Tentar enviar email de recuperação
                auth.sendPasswordResetEmail(email.trim()).await()
                
                _resetPasswordState.value = ResetPasswordState.Success(email.trim())
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("no user record", ignoreCase = true) == true ||
                    e.message?.contains("user not found", ignoreCase = true) == true -> 
                        "Este e-mail não está cadastrado no sistema."
                    e.message?.contains("invalid email", ignoreCase = true) == true -> 
                        "E-mail inválido. Verifique o formato do e-mail."
                    e.message?.contains("network", ignoreCase = true) == true -> 
                        "Erro de conexão. Verifique sua internet."
                    else -> "Erro ao enviar e-mail: ${e.message ?: "Erro desconhecido"}"
                }
                _resetPasswordState.value = ResetPasswordState.Error(errorMessage)
            }
        }
    }
    
    // Estados Selados para Login
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val userType: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }
    
    // Estados Selados para Registro
    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
    
    // Estados Selados para Recuperação de Senha
    sealed class ResetPasswordState {
        object Idle : ResetPasswordState()
        object Loading : ResetPasswordState()
        data class Success(val email: String) : ResetPasswordState()
        data class Error(val message: String) : ResetPasswordState()
    }
}
