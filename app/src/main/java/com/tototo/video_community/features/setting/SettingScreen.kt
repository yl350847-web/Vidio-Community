package com.tototo.video_community.features.setting

import android.os.Build
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tototo.video_community.data.local.SearchHistoryRepository
import com.tototo.video_community.ui.viewmodel.ThemeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onNavigateToSearch: (String) -> Unit,
    themeViewModel: ThemeViewModel = koinViewModel()
) {
    val isDark = themeViewModel.isDark.collectAsState().value
    val isDynamicColor = themeViewModel.isDynamicColor.collectAsState().value
    val dynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val historyRepo = koinInject<SearchHistoryRepository>()
    val recents = historyRepo.recentQueries.collectAsState(initial = emptyList()).value
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("深色模式", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "开启后界面将变为深色风格",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { themeViewModel.toggleDark() }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("动态取色", style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (dynamicSupported) "Android 12+ 可用，跟随壁纸取色"
                        else "仅 Android 12+ 可用",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = if (dynamicSupported) isDynamicColor else false,
                    onCheckedChange = { if (dynamicSupported) themeViewModel.toggleDynamicColor() },
                    enabled = dynamicSupported
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("最近搜索", style = MaterialTheme.typography.titleMedium)

                if (recents.isEmpty()) {
                    Text(
                        "暂无历史",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        recents.forEach { q ->
                            OutlinedButton(onClick = { onNavigateToSearch(q) }) {
                                Text(q)
                            }
                        }
                        OutlinedButton(onClick = { scope.launch { historyRepo.clear() } }) {
                            Text("清空历史")
                        }
                    }
                }
            }
        }
    }
}