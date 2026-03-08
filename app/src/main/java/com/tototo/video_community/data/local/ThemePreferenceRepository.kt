package com.tototo.video_community.data.local

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ThemePreferenceRepository(context: Context) {
    private val dataStore = PreferenceDataStoreFactory.create {
        context.dataStoreFile("app_settings.preferences_pb")
    }
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean> =
        dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs -> prefs[KEY_DARK_MODE] ?: false }

    suspend fun setDarkMode(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = value
        }
    }
}