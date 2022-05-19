package com.sahilpc.bookie.domain.usecases


import android.util.Patterns
import javax.inject.Inject

class ValidatePhone
@Inject
constructor() {
    operator fun invoke(phone: String):ValidationResult{
        if (phone.length < 10){
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number must contain 10 digits"
            )
        }

        return  ValidationResult(
            successful = true
        )
    }
}