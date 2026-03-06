package com.tototo.video_community.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    initialQuery: String,
    viewModel: SearchViewModel = koinViewModel()
) {
    val input = remember { mutableStateOf(initialQuery) }
    val lazyItems = viewModel.results.collectAsLazyPagingItems()

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.load(initialQuery)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("搜索关键字") }
        )
        Button(onClick = { viewModel.load(input.value) }) {
            Text("搜索")
        }

        when (val s = lazyItems.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }
            is LoadState.Error -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("加载失败：${s.error.message ?: "未知错误"}")
                    Button(onClick = { lazyItems.retry() }) {
                        Text("重试")
                    }
                }
            }
            is LoadState.NotLoading -> {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lazyItems.itemCount) { index ->
                        val item = lazyItems[index]
                        if (item != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { }
                            ) {
                                Text(item.title)
                                Text(item.desc)
                            }
                        }
                    }
                    when (val a = lazyItems.loadState.append) {
                        is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }
                        is LoadState.Error -> {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("更多内容加载失败：${a.error.message ?: "未知错误"}")
                                    Button(onClick = { lazyItems.retry() }) {
                                        Text("重试加载更多")
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}