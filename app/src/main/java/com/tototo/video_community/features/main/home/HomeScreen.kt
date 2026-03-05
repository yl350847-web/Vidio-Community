package com.tototo.video_community.features.main.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToSearch: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uiState.items) { item ->
            ItemCard(
                title = item.title,
                subtitle = item.subtitle,
                imageUrl = item.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSearch(item.title) }
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
        Column(horizontalAlignment = Alignment.Start) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                error = painterResource(android.R.drawable.ic_menu_report_image)
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