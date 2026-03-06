package com.tototo.video_community.features.main.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import com.tototo.video_community.data.repository.FakeSubscriptionRepository
import com.tototo.video_community.data.repository.SubscriptionItem

class SubscriptionViewModel(
    private val repo: FakeSubscriptionRepository
) : ViewModel() {
    val items: Flow<PagingData<SubscriptionItem>> =
        repo.pager().flow.cachedIn(viewModelScope)
}