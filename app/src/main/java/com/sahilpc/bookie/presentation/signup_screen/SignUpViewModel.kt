package com.sahilpc.bookie.presentation.signup_screen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilpc.bookie.domain.model.User
import com.sahilpc.bookie.domain.repository.AuthRepository
import com.sahilpc.bookie.domain.usecases.ValidateEmail
import com.sahilpc.bookie.domain.usecases.ValidatePassword
import com.sahilpc.bookie.domain.usecases.ValidatePhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val app: Application,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validatePhone: ValidatePhone
) : ViewModel() {

    var errorState  = MutableLiveData<SignupFormErrorState>()

    fun signUp(user: User) : Boolean{
        errorState.postValue(
            SignupFormErrorState(
                emailError = null,
                passwordError = null,
                phoneError = null
        ))

        val emailResult = validateEmail(user.email)
        val passwordResult = validatePassword(username = user.name , password = user.password)
        val phoneResult = validatePhone(user.phone)

        val hasError = listOf(
            emailResult,
            passwordResult,
            phoneResult
        ).any { !it.successful }

        if(hasError){
            errorState.postValue(SignupFormErrorState(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                phoneError = phoneResult.errorMessage
            )
            )
            return false
        }

        viewModelScope.launch {
            val result = authRepository.signUpUser(user)
            Toast.makeText(app, result, Toast.LENGTH_SHORT).show()
        }

        return true
    }

}