package com.tototo.video_community.features.main.subscription

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

private data class SubItem(
    val title: String,
    val imageUrl: String
)

@Composable
fun SubscriptionScreen() {
    val data = List(12) { index ->
        SubItem(
            title = "订阅项 $index",
            imageUrl = "https://picsum.photos/seed/sub$index/800/400"
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(data) { item ->
            GridItemCard(
                title = item.title,
                imageUrl = item.imageUrl
            )
        }
    }
}

@Composable
private fun GridItemCard(
    title: String,
    imageUrl: String
) {
    Surface {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Text(title, style = MaterialTheme.typography.titleMedium)
    }
}