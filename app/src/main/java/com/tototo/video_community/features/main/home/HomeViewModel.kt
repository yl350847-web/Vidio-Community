package com.tototo.video_community.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tototo.video_community.data.repository.VideoRepository
import com.tototo.video_community.model.VideoDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class HomeUiState(
    val items: List<VideoDto> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repo: VideoRepository
) : ViewModel() {
    private var _uiState = androidx.compose.runtime.mutableStateOf(HomeUiState())
    val uiState: androidx.compose.runtime.State<HomeUiState> get() = _uiState

    init {
        reload()
    }

    fun reload() {
        _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
        viewModelScope.launch {
            delay(600)
            try {
                val list = repo.getAll()
                _uiState.value = HomeUiState(items = list, isRefreshing = false, errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = e.message ?: "加载失败"
                )
            }
        }
    }
}