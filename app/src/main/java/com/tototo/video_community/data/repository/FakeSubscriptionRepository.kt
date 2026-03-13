package com.tototo.video_community.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig

data class SubscriptionItem(
    val id: String,
    val title: String,
    val coverUrl: String
)

class FakeSubscriptionRepository(
    private val videoRepository: VideoRepository
) {
    fun pager(): Pager<Int, SubscriptionItem> {
        val items = videoRepository.getAll().map { video ->
            SubscriptionItem(
                id = video.id,
                title = video.title,
                coverUrl = video.coverUrl
            )
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 10
            )
        ) {
            FakeSubscriptionPagingSource(items)
        }
    }
}
