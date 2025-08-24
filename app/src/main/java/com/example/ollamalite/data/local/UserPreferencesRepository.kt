package com.example.ollamalite.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val SERVER_URL = stringPreferencesKey("server_url")
        val SELECTED_MODEL = stringPreferencesKey("selected_model")
    }

    val serverUrl: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SERVER_URL] ?: "http://127.0.0.1:11434"
        }

    val selectedModel: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SELECTED_MODEL]
        }

    suspend fun saveServerUrl(serverUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_URL] = serverUrl
        }
    }

    suspend fun saveSelectedModel(model: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_MODEL] = model
        }
    }
}
