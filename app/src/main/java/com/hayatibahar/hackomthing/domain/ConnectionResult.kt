package com.hayatibahar.hackomthing.domain

sealed interface ConnectionResult {
    data object ConnectionEstablished : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}