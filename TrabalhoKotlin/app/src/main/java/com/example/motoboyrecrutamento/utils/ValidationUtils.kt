package com.example.motoboyrecrutamento.utils

import android.util.Patterns

/**
 * Utilitários para validação de dados
 */
object ValidationUtils {
    
    /**
     * Valida se o e-mail tem formato válido
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Valida se a senha atende aos requisitos mínimos
     * - Mínimo de 6 caracteres
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
    
    /**
     * Valida se as senhas coincidem
     */
    fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
    
    /**
     * Valida se o nome não está vazio
     */
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 3
    }
    
    /**
     * Valida se o CNPJ tem formato básico válido (apenas números, 14 dígitos)
     */
    fun isValidCNPJ(cnpj: String): Boolean {
        val cleanCNPJ = cnpj.replace(Regex("[^0-9]"), "")
        return cleanCNPJ.length == 14
    }
    
    /**
     * Valida se a CNH tem formato básico válido (11 dígitos)
     */
    fun isValidCNH(cnh: String): Boolean {
        val cleanCNH = cnh.replace(Regex("[^0-9]"), "")
        return cleanCNH.length == 11
    }
    
    /**
     * Valida se o telefone tem formato básico válido (10 ou 11 dígitos)
     */
    fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return cleanPhone.length in 10..11
    }
}
