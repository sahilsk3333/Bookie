package com.sahilpc.bookie.presentation.signup_screen

data class SignupFormErrorState(
    val emailError:String? = null,
    val passwordError:String? = null,
    val phoneError:String? = null
)
