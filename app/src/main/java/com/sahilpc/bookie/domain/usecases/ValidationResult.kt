package com.sahilpc.bookie.domain.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
