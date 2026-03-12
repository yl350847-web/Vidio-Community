package com.tototo.video_community.data.local

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class VideoQuality(val code: String, val label: String) {
    AUTO("AUTO", "自动"),
    P360("360P", "360P"),
    P480("480P", "480P"),
    P720("720P", "720P"),
    P1080("1080P", "1080P")
}

class VideoPreferencesRepository(context: Context) {
    private val dataStore = PreferenceDataStoreFactory.create {
        context.dataStoreFile("video_prefs.preferences_pb")
    }

    private val KEY_MOBILE_QUALITY = stringPreferencesKey("mobile_quality")
    private val KEY_WLAN_QUALITY = stringPreferencesKey("wlan_quality")

    val mobileQuality: Flow<VideoQuality> =
        dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs ->
                val code = prefs[KEY_MOBILE_QUALITY] ?: VideoQuality.AUTO.code
                VideoQuality.values().find { it.code == code } ?: VideoQuality.AUTO
            }

    val wlanQuality: Flow<VideoQuality> =
        dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs ->
                val code = prefs[KEY_WLAN_QUALITY] ?: VideoQuality.AUTO.code
                VideoQuality.values().find { it.code == code } ?: VideoQuality.AUTO
            }

    suspend fun setMobileQuality(q: VideoQuality) {
        dataStore.edit { prefs -> prefs[KEY_MOBILE_QUALITY] = q.code }
    }

    suspend fun setWlanQuality(q: VideoQuality) {
        dataStore.edit { prefs -> prefs[KEY_WLAN_QUALITY] = q.code }
    }
}