package com.tototo.video_community.features.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.data.repository.SearchItem

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val items: List<SearchItem> = emptyList()
)

class SearchViewModel(
    private val repo: FakeSearchRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun load(query: String) {
        _uiState.value = _uiState.value.copy(query = query, isLoading = true)
        val result = repo.search(query)
        _uiState.value = _uiState.value.copy(isLoading = false, items = result)
    }
}