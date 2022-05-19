package com.sahilpc.bookie.domain.usecases


import android.util.Patterns
import javax.inject.Inject

class ValidateEmail
@Inject
constructor() {
    operator fun invoke(email: String):ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }
        return  ValidationResult(
            successful = true
        )
    }
}