package com.sahilpc.bookie.domain.usecases


import android.util.Patterns
import javax.inject.Inject

class ValidatePassword
@Inject
constructor() {

    operator fun invoke(username: String, password: String): ValidationResult {

        if (password.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "password must not be blank"
            )
        }

        if (password[0].isUpperCase()){
            return ValidationResult(
                successful = false,
                errorMessage = "first character should be lowercase"
            )
        }

        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }

        if (password.length > 15) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must be less than 15 digits"
            )
        }

        val containsUserName = password.contains(username)
        if (containsUserName){
            return ValidationResult(
                successful = false,
                errorMessage = "Password must not contain username"
            )
        }

        val containsLettersAndDigits = password.count { it.isDigit() } < 2 &&
                password.any { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password must contain at least a letter and 2 digits"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}