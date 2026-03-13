package com.tototo.video_community.features.main.home

import android.net.Uri
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tototo.video_community.nav.AppRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToSearch: (String) -> Unit,
    onNavigateToVideoDetail: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.reload() }) { Text("刷新") }
            if (uiState.isRefreshing) {
                CircularProgressIndicator()
            }
        }

        when {
            uiState.isRefreshing || (uiState.items.isEmpty() && uiState.errorMessage == null) -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) { SkeletonCard() }
                }
            }
            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(uiState.errorMessage ?: "未知错误")
                    OutlinedButton(onClick = { viewModel.reload() }) { Text("重试") }
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.items) { video ->
                        VideoCard(
                            title = video.title,
                            desc = video.desc,
                            coverUrl = video.coverUrl,
                            onClick = {
                                val route = "${AppRoute.VideoDetail}?id=${Uri.encode(video.id)}"
                                onNavigateToVideoDetail(route)
                            },
                            onSearchClick = { onNavigateToSearch(video.title) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoCard(
    title: String,
    desc: String,
    coverUrl: String,
    onClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Column {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(desc, style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = onSearchClick) { Text("用标题搜索") }
            }
        }
    }
}

@Composable
private fun SkeletonCard() {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    ).value
    val baseColor = MaterialTheme.colorScheme.surfaceVariant

    Surface {
        Column {
            Box(Modifier.fillMaxWidth().height(160.dp).background(baseColor.copy(alpha = alpha)))
            Box(Modifier.fillMaxWidth().height(20.dp).background(baseColor.copy(alpha = alpha)))
            Box(Modifier.fillMaxWidth().height(16.dp).background(baseColor.copy(alpha = alpha)))
        }
    }
}

@Composable
private fun Box(modifier: Modifier) {
    androidx.compose.foundation.layout.Box(modifier = modifier)
}