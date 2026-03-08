package com.tototo.video_community.features.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tototo.video_community.AppState
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    onNavigateToSetting: () -> Unit
) {
    val sharedViewModel = koinInject<SharedViewModel>()

    // 模拟的设置项列表
    val data = listOf("我的收藏", "历史记录", "下载管理")

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部用户信息区域（简易版）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "未登录用户", // 后续可接入 UserViewModel
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            // 设置入口图标
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "设置",
                modifier = Modifier.clickable { onNavigateToSetting() }
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data) { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    modifier = Modifier.clickable { }
                )
            }
        }

        // 底部退出登录
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { sharedViewModel.updateAppState(AppState(isLogin = false)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("退出登录")
            }
        }
    }
}