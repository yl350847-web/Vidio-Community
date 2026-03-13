package com.tototo.video_community.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FakeSearchPagingSource(
    private val query: String,
    private val videoRepository: VideoRepository
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

        val matched = videoRepository.getAll()
            .filter { it.title.contains(q, ignoreCase = true) }
            .map { video ->
                SearchItem(
                    id = video.id,
                    title = video.title,
                    desc = video.desc,
                    coverUrl = video.coverUrl
                )
            }

        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)
        val start = (page - 1) * pageSize
        val endExclusive = (start + pageSize).coerceAtMost(matched.size)

        val data = if (start >= matched.size) emptyList() else matched.subList(start, endExclusive)
        val nextKey = if (endExclusive < matched.size) page + 1 else null

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