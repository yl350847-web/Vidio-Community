package com.tototo.video_community.features.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tototo.video_community.AppState
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.compose.koinInject

@Composable
fun ProfileScreen() {
    val sharedViewModel = koinInject<SharedViewModel>()
    val data = listOf("我的收藏", "历史记录", "下载管理")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing())
        ) {
            items(data) { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { sharedViewModel.updateAppState(AppState(isLogin = false)) }
            ) {
                Text("退出登录")
            }
        }
    }
}

private fun MaterialTheme.spacing(): Dp = 12.dp