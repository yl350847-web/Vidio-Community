package com.tototo.video_community.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FakeSearchPagingSource(
    private val query: String
) : PagingSource<Int, SearchItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val q = query.trim()
        if (q.isEmpty()) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)
        val start = (page - 1) * pageSize
        val data = List(pageSize) { i ->
            val idx = start + i
            SearchItem(title = "$q 的结果 $idx", desc = "这是演示数据 $idx")
        }
        val nextKey = if (data.isEmpty()) null else page + 1
        return LoadResult.Page(
            data = data,
            prevKey = if (page > 1) page - 1 else null,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }
}