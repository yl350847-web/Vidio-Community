package com.tototo.video_community.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FakeSubscriptionPagingSource(
    private val items: List<SubscriptionItem>
) : PagingSource<Int, SubscriptionItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubscriptionItem> {
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)
        val start = (page - 1) * pageSize
        val endExclusive = (start + pageSize).coerceAtMost(items.size)
        val data = if (start >= items.size) emptyList() else items.subList(start, endExclusive)
        val nextKey = if (endExclusive < items.size) page + 1 else null
        return LoadResult.Page(
            data = data,
            prevKey = if (page > 1) page - 1 else null,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, SubscriptionItem>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }
}
