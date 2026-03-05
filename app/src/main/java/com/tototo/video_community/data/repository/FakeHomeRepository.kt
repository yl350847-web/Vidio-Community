package com.tototo.video_community.data.repository

data class HomeItem(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

class FakeHomeRepository {
    fun getHomeItems(): List<HomeItem> = listOf(
        HomeItem("推荐视频", "为你定制", "https://picsum.photos/seed/recommend/800/400"),
        HomeItem("热门视频", "全站热度", "https://picsum.photos/seed/hot/800/400"),
        HomeItem("专题合集", "主题内容", "https://picsum.photos/seed/topic/800/400"),
        HomeItem("示例：跳转搜索", "演示跨路由", "https://picsum.photos/seed/search/800/400")
    )
}