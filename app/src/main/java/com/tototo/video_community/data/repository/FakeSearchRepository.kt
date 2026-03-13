package com.tototo.video_community.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig

data class SearchItem(
    val id: String,
    val title: String,
    val desc: String,
    val coverUrl: String
)

class FakeSearchRepository(
    private val videoRepository: VideoRepository
) {
    fun pagerFor(query: String): Pager<Int, SearchItem> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 10
            )
        ) {
            FakeSearchPagingSource(query, videoRepository)
        }
    }
}
