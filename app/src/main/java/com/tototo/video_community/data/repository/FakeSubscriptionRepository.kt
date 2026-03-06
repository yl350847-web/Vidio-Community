package com.tototo.video_community.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig

data class SubscriptionItem(
    val title: String,
    val imageUrl: String
)

class FakeSubscriptionRepository {
    fun pager(): Pager<Int, SubscriptionItem> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 10
            )
        ) {
            FakeSubscriptionPagingSource()
        }
    }
}