package com.tototo.video_community.features.main.subscription

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = koinViewModel()
) {
    val lazyItems = viewModel.items.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(lazyItems.itemCount) { index ->
            val item = lazyItems[index]
            if (item != null) {
                GridItemCard(
                    title = item.title,
                    imageUrl = item.imageUrl
                )
            }
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
                .height(120.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(android.R.drawable.ic_menu_report_image),
            error = painterResource(android.R.drawable.ic_menu_report_image)
        )
        Text(title, style = MaterialTheme.typography.titleMedium)
    }
}