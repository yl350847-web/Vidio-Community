package com.tototo.video_community.features.main.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToSearch: (String) -> Unit,
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
                // 刷新中或首次加载中：显示骨架占位
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) { SkeletonCard() }
                }
            }
            uiState.errorMessage != null -> {
                // 错误态：显示文案 + 重试
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
                // 正常内容
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
                contentScale = ContentScale.Crop
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

/**
 * 骨架卡片：使用 alpha 脉动模拟“在加载中”的视觉效果
 * 不引入外部依赖，仅用 Compose 的 infiniteTransition
 */
@Composable
private fun SkeletonCard() {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val baseColor = MaterialTheme.colorScheme.surfaceVariant

    Surface {
        Column {
            // 顶部图片占位
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(baseColor.copy(alpha = alpha))
            )
            // 标题占位
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(baseColor.copy(alpha = alpha))
            )
            // 副标题占位
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(baseColor.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun Box(modifier: Modifier) {
    androidx.compose.foundation.layout.Box(modifier = modifier)
}