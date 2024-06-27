package com.example.storyapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.remote.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SessionPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveLoginData(loginResponse: LoginResult) {
        dataStore.edit { preferences ->
            preferences[KEY_NAME] = loginResponse.name
            preferences[PREF_USER_ID] = loginResponse.userId
            preferences[KEY_TOKEN] = loginResponse.token
        }
    }

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map { preference ->
            val token = preference[KEY_TOKEN] ?: ""
            LoginResult(
                preference[KEY_NAME] ?: "",
                preference[PREF_USER_ID] ?: "",
                token
            )

        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        private val KEY_NAME = stringPreferencesKey("name")
        private val PREF_USER_ID = stringPreferencesKey("userId")
        private val KEY_TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
