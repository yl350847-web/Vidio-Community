package com.tototo.video_community.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tototo.video_community.data.local.ThemePreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val repo: ThemePreferenceRepository
) : ViewModel() {
    val isDark: StateFlow<Boolean> =
        repo.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isDynamicColor: StateFlow<Boolean> =
        repo.isDynamicColor.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleDark() {
        viewModelScope.launch {
            repo.setDarkMode(!isDark.value)
        }
    }

    fun toggleDynamicColor() {
        viewModelScope.launch {
            repo.setDynamicColor(!isDynamicColor.value)
        }
    }

    fun setDynamicColor(value: Boolean) {
        viewModelScope.launch {
            repo.setDynamicColor(value)
        }
    }
}