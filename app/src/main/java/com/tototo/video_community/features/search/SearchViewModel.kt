package com.tototo.video_community.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.data.repository.SearchItem

class SearchViewModel(
    private val repo: FakeSearchRepository
) : ViewModel() {
    private val queryFlow = MutableStateFlow("")

    val results: Flow<PagingData<SearchItem>> =
        queryFlow.flatMapLatest { q ->
            repo.pagerFor(q).flow
        }.cachedIn(viewModelScope)

    fun load(query: String) {
        queryFlow.value = query
    }
}