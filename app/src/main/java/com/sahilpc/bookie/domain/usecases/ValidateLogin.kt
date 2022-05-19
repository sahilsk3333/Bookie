package com.sahilpc.bookie.domain.usecases

import android.util.Patterns
import at.favre.lib.crypto.bcrypt.BCrypt
import javax.inject.Inject

class ValidateLogin @Inject
constructor() {
    operator fun invoke(password: String, passwordHash: String): ValidationResult {

        val result = BCrypt.verifyer().verify(password.toCharArray(),passwordHash)

        if (password.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Password must be not empty"
            )
        }

        if (!result.verified){
            return  ValidationResult(
                successful = false,
                errorMessage = "Password does not match"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}