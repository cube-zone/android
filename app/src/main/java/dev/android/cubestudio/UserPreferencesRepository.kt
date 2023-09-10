package dev.android.cubestudio

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences (
    val currentSessionId: Int,
    val currentScrambleType: String,
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val CURRENT_SESSION_ID = intPreferencesKey("current_session_id")
        val CURRENT_SCRAMBLE_TYPE = stringPreferencesKey("current_scramble_type")
    }
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("SettingsRepo", "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }
    suspend fun updateCurrentSessionId(currentSessionId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_SESSION_ID] = currentSessionId
        }
    }suspend fun updateCurrentScrambleType(currentScrambleType: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_SCRAMBLE_TYPE] = currentScrambleType
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val currentSessionId = preferences[PreferencesKeys.CURRENT_SESSION_ID] ?: 0
        val currentScrambleType = preferences[PreferencesKeys.CURRENT_SCRAMBLE_TYPE] ?: "3x3"
        return UserPreferences(currentSessionId, currentScrambleType)
    }
}