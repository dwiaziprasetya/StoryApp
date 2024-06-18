import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SessionPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {

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

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map { preference ->
            LoginResult(
                preference[KEY_NAME] ?: "",
                preference[PREF_USER_ID] ?: "",
                preference[KEY_TOKEN] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveLoginData(loginResponse: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[KEY_NAME] = loginResponse.loginResult.name
            preferences[PREF_USER_ID] = loginResponse.loginResult.userId
            preferences[KEY_TOKEN] = loginResponse.loginResult.token
        }
    }

    fun getName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }
    }
}
