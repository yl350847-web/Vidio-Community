package com.tototo.video_community.features.main.subscription

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = koinViewModel()
) {
    val lazyItems = viewModel.items.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 顶部刷新按钮（触发 Pagination 的 refresh）
        Button(onClick = { lazyItems.refresh() }) { Text("刷新") }

        when (val s = lazyItems.loadState.refresh) {
            is LoadState.Loading -> {
                // 显示骨架网格
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(6) { GridSkeletonCard() }
                }
            }
            is LoadState.Error -> {
                ColumnError(
                    message = "加载失败：" + (s.error.message ?: "未知错误"),
                    onRetry = { lazyItems.retry() }
                )
            }
            is LoadState.NotLoading -> {
                if (lazyItems.itemCount == 0) {
                    ColumnEmpty(onRetry = { lazyItems.refresh() })
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(lazyItems.itemCount) { index ->
                            val item = lazyItems[index]
                            if (item != null) {
                                GridItemCard(title = item.title, imageUrl = item.imageUrl)
                            }
                        }
                        when (val a = lazyItems.loadState.append) {
                            is LoadState.Loading -> {
                                item(span = { GridItemSpan(2) }) { CircularProgressIndicator() }
                            }
                            is LoadState.Error -> {
                                item(span = { GridItemSpan(2) }) {
                                    ColumnError(
                                        message = "更多内容加载失败：" + (a.error.message ?: "未知错误"),
                                        onRetry = { lazyItems.retry() }
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnError(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(message)
        OutlinedButton(onClick = onRetry) { Text("重试") }
    }
}

@Composable
private fun ColumnEmpty(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("暂无内容")
        OutlinedButton(onClick = onRetry) { Text("重试") }
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
            contentScale = ContentScale.Crop
        )
        Text(title, style = MaterialTheme.typography.titleMedium)
    }
}

/**
 * 网格骨架卡片：上方大图占位 + 下方一条标题占位
 */
@Composable
private fun GridSkeletonCard() {
    val transition = rememberInfiniteTransition(label = "gridSkeleton")
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
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(baseColor.copy(alpha = alpha))
        )
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(baseColor.copy(alpha = alpha))
        )
    }
}