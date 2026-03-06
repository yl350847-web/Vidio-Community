package com.tototo.video_community.data.local

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SearchHistoryRepository(context: Context) {
    private val dataStore = PreferenceDataStoreFactory.create {
        context.dataStoreFile("search_history.preferences_pb")
    }
    private val KEY_RECENTS_STR = stringPreferencesKey("recents_str")

    val recentQueries: Flow<List<String>> =
        dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs ->
                val raw = prefs[KEY_RECENTS_STR].orEmpty()
                raw.split("|").filter { it.isNotBlank() }
            }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun addQuery(q: String) {
        val query = q.trim()
        if (query.isEmpty()) return
        dataStore.edit { prefs ->
            val raw = prefs[KEY_RECENTS_STR].orEmpty()
            val list = raw.split("|").filter { it.isNotBlank() }.toMutableList()
            list.remove(query)
            list.add(0, query)
            while (list.size > 10) list.removeLast()
            prefs[KEY_RECENTS_STR] = list.joinToString("|")
        }
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs[KEY_RECENTS_STR] = ""
        }
    }
}