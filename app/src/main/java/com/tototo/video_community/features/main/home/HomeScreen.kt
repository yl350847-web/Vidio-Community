package com.tototo.video_community.features.main.home

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

private data class HomeItem(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit
) {
    val data = listOf(
        HomeItem(
            title = "推荐视频",
            subtitle = "为你定制",
            imageUrl = "https://picsum.photos/seed/recommend/800/400"
        ),
        HomeItem(
            title = "热门视频",
            subtitle = "全站热度",
            imageUrl = "https://picsum.photos/seed/hot/800/400"
        ),
        HomeItem(
            title = "专题合集",
            subtitle = "主题内容",
            imageUrl = "https://picsum.photos/seed/topic/800/400"
        ),
        HomeItem(
            title = "示例：跳转搜索",
            subtitle = "演示跨路由",
            imageUrl = "https://picsum.photos/seed/search/800/400"
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data) { item ->
            ItemCard(
                title = item.title,
                subtitle = item.subtitle,
                imageUrl = item.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (item.title.contains("示例")) {
                            onNavigateToSearch()
                        }
                    }
            )
        }
    }
}

@Composable
private fun ItemCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_menu_report_image),
                error = painterResource(R.drawable.ic_menu_report_image)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}