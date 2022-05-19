package com.sahilpc.bookie.presentation.signin_screen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilpc.bookie.domain.repository.AuthRepository
import com.sahilpc.bookie.domain.usecases.ValidateEmail
import com.sahilpc.bookie.domain.usecases.ValidateLogin
import com.sahilpc.bookie.domain.usecases.ValidatePassword
import com.sahilpc.bookie.domain.usecases.ValidatePhone
import com.sahilpc.bookie.presentation.signup_screen.SignupFormErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val app: Application,
    private val validateLogin: ValidateLogin
) : ViewModel() {

    var errorState = MutableLiveData<SignInFormErrorState>()
    var isLoginSuccess = MutableLiveData<Boolean>()

    init {
        isUserAlreadyLoggedIn()
    }

    private fun isUserAlreadyLoggedIn(){
        viewModelScope.launch {
            val result = authRepository.getLoggedInUser()
            if (!(result == null || result == "")){
                isLoginSuccess.postValue(true)
            }
        }
    }

    fun signIn(user: String, password: String) {

        errorState.postValue(
            SignInFormErrorState(
                userError = null,
                passwordError = null
            )
        )

        if (user.isBlank()){
            errorState.postValue(
                SignInFormErrorState(
                   userError = "Field must not be empty"
                )
            )
            return
        }

        viewModelScope.launch {
            val result = authRepository.signInUser(user)

            if (result == null) {
                errorState.postValue(
                    SignInFormErrorState(
                        userError = "User does not exist"
                    )
                )
                return@launch
            }

            val isPasswordMatch = validateLogin(password, result.password)

            if (!isPasswordMatch.successful) {
                errorState.postValue(
                    SignInFormErrorState(
                        passwordError = isPasswordMatch.errorMessage
                    )
                )
                return@launch
            }

            if (isPasswordMatch.successful) {
                isLoginSuccess.postValue(true)
                authRepository.setLoggedInUser(result.email)
                Toast.makeText(app, "Welcome ${result.name}", Toast.LENGTH_SHORT).show()
            }

        }

    }

}