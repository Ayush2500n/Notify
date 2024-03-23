package com.example.notify.notifyNotes.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.notify.notifyNotes.utils.Constants.EMAIL_KEY
import com.example.notify.notifyNotes.utils.Constants.JWT_TOKEN_KEY
import com.example.notify.notifyNotes.utils.Constants.NAME_KEY
import kotlinx.coroutines.flow.first

class sessionManager(val context: Context) {
    private val Context.datastore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore("session_manager")
    suspend fun updateSession(token: String, email: String, name: String){
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val nameKey = stringPreferencesKey(NAME_KEY)
        val emailKey = stringPreferencesKey(EMAIL_KEY)

        context.datastore.edit {
            it[jwtTokenKey] = token
            it[nameKey] = name
            it[emailKey] = email
        }
    }
    suspend fun getJWTToken(): String?{
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.datastore.data.first()
        return preferences[jwtTokenKey]
    }
    suspend fun getCurrentName(): String?{
        val nameKey = stringPreferencesKey(NAME_KEY)
        val preferences = context.datastore.data.first()
        return preferences[nameKey]
    }
    suspend fun getCurrentEmail(): String?{
        val emailKey = stringPreferencesKey(EMAIL_KEY)
        val preferences = context.datastore.data.first()
        return preferences[emailKey]
    }
    suspend fun logout(){
        context.datastore.edit {
            it.clear()
        }
    }
}