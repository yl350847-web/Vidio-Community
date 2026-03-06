package com.tototo.video_community.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.data.repository.SearchItem
import com.tototo.video_community.data.local.SearchHistoryRepository

class SearchViewModel(
    private val repo: FakeSearchRepository,
    private val historyRepo: SearchHistoryRepository
) : ViewModel() {
    private val queryFlow = MutableStateFlow("")
    val results: Flow<PagingData<SearchItem>> =
        queryFlow.flatMapLatest { q ->
            repo.pagerFor(q).flow
        }.cachedIn(viewModelScope)

    val recentQueries: Flow<List<String>> = historyRepo.recentQueries

    fun load(query: String) {
        queryFlow.value = query
        viewModelScope.launch {
            historyRepo.addQuery(query)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepo.clear()
        }
    }
}