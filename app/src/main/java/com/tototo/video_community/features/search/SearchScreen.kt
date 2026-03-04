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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class ResultItem(
    val title: String,
    val desc: String
)

@Composable
fun SearchScreen() {
    val query = remember { mutableStateOf("") }
    val results = remember { mutableStateOf(listOf<ResultItem>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("搜索关键字") }
        )
        Button(
            onClick = {
                val q = query.value.trim()
                results.value = when {
                    q.isEmpty() -> emptyList()
                    else -> List(10) { i ->
                        ResultItem(title = "$q 的结果 $i", desc = "这是演示数据 $i")
                    }
                }
            }
        ) {
            Text("搜索")
        }

        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(results.value) { item ->
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