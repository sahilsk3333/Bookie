package com.sahilpc.bookie.domain.repository

import com.sahilpc.bookie.domain.model.User

interface AuthRepository {
    suspend fun signInUser(user: String): User?
    suspend fun signUpUser(user: User): String
    suspend fun getLoggedInUser(): String?
    suspend fun logOutUser()
    suspend fun setLoggedInUser(email: String)
}