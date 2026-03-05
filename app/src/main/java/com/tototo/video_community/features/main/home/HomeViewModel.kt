package com.tototo.video_community.features.main.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.tototo.video_community.data.repository.FakeHomeRepository
import com.tototo.video_community.data.repository.HomeItem

data class HomeUiState(
    val items: List<HomeItem> = emptyList()
)

class HomeViewModel(
    private val repo: FakeHomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        load()
    }

    fun load() {
        _uiState.value = HomeUiState(items = repo.getHomeItems())
    }
}