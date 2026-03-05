package com.tototo.video_community.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    initialQuery: String,
    viewModel: SearchViewModel = koinViewModel()
) {
    val ui = viewModel.uiState.collectAsState().value
    val input = remember { mutableStateOf(initialQuery) }

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
        Button(
            onClick = { viewModel.load(input.value) }
        ) {
            Text("搜索")
        }

        if (ui.items.isEmpty() && ui.query.isNotBlank()) {
            Text("没有找到与“${ui.query}”相关的结果")
        }

        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ui.items) { item ->
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
    }
}