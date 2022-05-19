package com.sahilpc.bookie.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDatastore @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(
        name = AUTH_DATASTORE_NAME
    )

    //Marks the user as logged in
    suspend fun setUserLoggedIn(email : String) {
        val dataStoreKey = stringPreferencesKey(AUTH_DATASTORE_KEY)
        context.dataStore.edit {
            it[dataStoreKey] = email
        }
    }

    //Checks if the user is logged in
    suspend fun getLoggedInUser(): String? {
        val dataStoreKey = stringPreferencesKey(AUTH_DATASTORE_KEY)
        val loginStatus = context.dataStore.data.first()
        return loginStatus[dataStoreKey]
    }

    //Marks the user as logged out
    suspend fun logOutUser() {
        val dataStoreKey = stringPreferencesKey(AUTH_DATASTORE_KEY)
        context.dataStore.edit { token ->
            token[dataStoreKey] = ""
        }
    }


    companion object{
        const val AUTH_DATASTORE_NAME = "Auth DataStore"
        const val AUTH_DATASTORE_KEY = "Auth DataStore Key"
    }

}