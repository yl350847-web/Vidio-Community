package com.tototo.video_community.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.tototo.video_community.data.local.ThemePreferenceRepository

class ThemeViewModel(
    private val repo: ThemePreferenceRepository
) : ViewModel() {
    val isDark: StateFlow<Boolean> =
        repo.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggle() {
        viewModelScope.launch {
            repo.setDarkMode(!isDark.value)
        }
    }
}