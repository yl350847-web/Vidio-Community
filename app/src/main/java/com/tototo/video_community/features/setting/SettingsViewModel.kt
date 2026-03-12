package com.tototo.video_community.features.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tototo.video_community.data.local.VideoPreferencesRepository
import com.tototo.video_community.data.local.VideoQuality
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: VideoPreferencesRepository
) : ViewModel() {

    val mobileQuality: StateFlow<VideoQuality> =
        repo.mobileQuality.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            VideoQuality.AUTO
        )

    val wlanQuality: StateFlow<VideoQuality> =
        repo.wlanQuality.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            VideoQuality.AUTO
        )

    fun setMobileQuality(q: VideoQuality) {
        viewModelScope.launch {
            repo.setMobileQuality(q)
        }
    }

    fun setWlanQuality(q: VideoQuality) {
        viewModelScope.launch {
            repo.setWlanQuality(q)
        }
    }
}