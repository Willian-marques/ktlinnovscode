package com.example.motoboyrecrutamento.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilitários para formatação de dados
 */
object FormatUtils {
    
    /**
     * Formata um valor Double para moeda brasileira (R$)
     */
    fun formatCurrency(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return format.format(value)
    }
    
    /**
     * Formata uma data ISO 8601 para formato brasileiro (dd/MM/yyyy)
     */
    fun formatDate(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val date = inputFormat.parse(isoDate)
            date?.let { outputFormat.format(it) } ?: isoDate
        } catch (e: Exception) {
            isoDate
        }
    }
    
    /**
     * Formata uma data e hora ISO 8601 para formato brasileiro (dd/MM/yyyy HH:mm)
     */
    fun formatDateTime(isoDateTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            val date = inputFormat.parse(isoDateTime)
            date?.let { outputFormat.format(it) } ?: isoDateTime
        } catch (e: Exception) {
            isoDateTime
        }
    }
    
    /**
     * Retorna a data/hora atual em formato ISO 8601
     */
    fun getCurrentDateTime(): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }
}
