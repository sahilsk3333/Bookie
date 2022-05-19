package com.sahilpc.bookie.data.repository

import android.util.Log
import com.sahilpc.bookie.data.local.AppDatabase
import com.sahilpc.bookie.data.local.datastore.AuthDatastore
import com.sahilpc.bookie.data.mapper.toUser
import com.sahilpc.bookie.data.mapper.toUserEntity
import com.sahilpc.bookie.domain.model.User
import com.sahilpc.bookie.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val authDatastore: AuthDatastore
) : AuthRepository {
    override suspend fun signInUser(user: String): User? {
        return try {
            val result = db.getUserDao().loginUser(user)
            if (result != null) {
                return User(
                    name = result.name,
                    password = result.password,
                    email = result.email,
                    phone = result.phone
                )
            }
            null
        } catch (e: Exception) {
            Log.d("Auth", "error")
            e.printStackTrace()
            null
        }
    }

    override suspend fun signUpUser(user: User): String {
        return try {
            db.getUserDao().registerUser(user.toUserEntity())
            "Signed Up successfully"
        } catch (e: Exception) {
            e.printStackTrace()
            "Something Went Wrong"
        }
    }

    override suspend fun setLoggedInUser(email: String) = authDatastore.setUserLoggedIn(email)
    override suspend fun getLoggedInUser(): String? = authDatastore.getLoggedInUser()
    override suspend fun logOutUser() = authDatastore.logOutUser()
}