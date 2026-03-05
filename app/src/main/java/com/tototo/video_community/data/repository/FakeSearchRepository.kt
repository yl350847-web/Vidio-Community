package com.tototo.video_community.data.repository

data class SearchItem(
    val title: String,
    val desc: String
)

class FakeSearchRepository {
    fun search(query: String): List<SearchItem> {
        val q = query.trim()
        if (q.isEmpty()) return emptyList()
        return List(10) { i ->
            SearchItem(title = "$q 的结果 $i", desc = "这是演示数据 $i")
        }
    }
}