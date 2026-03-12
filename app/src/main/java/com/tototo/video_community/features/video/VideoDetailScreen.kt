package com.tototo.video_community.features.video

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.media3.ui.PlayerView
import com.tototo.video_community.data.local.VideoQuality
import com.tototo.video_community.features.setting.SettingsViewModel
import com.tototo.video_community.ui.util.SystemUiUtil
import org.koin.androidx.compose.koinViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    title: String,
    coverUrl: String,
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val controller = remember { VideoPlayerController(context) }

    val wlanQuality = settingsViewModel.wlanQuality.collectAsState().value
    val mobileQuality = settingsViewModel.mobileQuality.collectAsState().value

    val sources = remember { VideoQualitySource.sources() }

    var showSheet by remember { mutableStateOf(false) }
    var selectedQuality by remember { mutableStateOf(VideoQuality.P720) }
    var isFullscreen by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { controller.release() }
    }

    LaunchedEffect(wlanQuality) {
        val q = if (wlanQuality == VideoQuality.AUTO) VideoQuality.P720 else wlanQuality
        selectedQuality = q
        val url = sources.firstOrNull { it.quality == q }?.url ?: sources.first().url
        controller.play(url)
    }

    BackHandler(enabled = isFullscreen) {
        val activity = context as Activity
        SystemUiUtil.exitFullScreen(activity)
        isFullscreen = false
    }

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = { Text("视频详情") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!isFullscreen) {
                Text(title.ifBlank { "未命名视频" }, style = MaterialTheme.typography.titleLarge)
            }

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = controller.player
                        useController = true
                    }
                },
                modifier = if (isFullscreen) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                }
            )

            if (!isFullscreen) {
                Text(
                    "默认画质偏好：WLAN=${wlanQuality.label}，蜂窝=${mobileQuality.label}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { showSheet = true }) {
                        Text("清晰度：${selectedQuality.label}")
                    }
                    Button(onClick = {
                        val activity = context as Activity
                        SystemUiUtil.enterFullScreen(activity)
                        isFullscreen = true
                    }) {
                        Text("全屏")
                    }
                    Button(onClick = { controller.pause() }) {
                        Text("暂停")
                    }
                }

                Text(
                    "说明：这里用不同 sample 视频 URL 模拟清晰度切换，后续接真实多码率地址。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("选择清晰度", style = MaterialTheme.typography.titleMedium)

                sources.forEach { src ->
                    ListItem(
                        headlineContent = { Text(src.quality.label) },
                        trailingContent = {
                            RadioButton(
                                selected = selectedQuality == src.quality,
                                onClick = {
                                    selectedQuality = src.quality
                                    controller.play(src.url)
                                    showSheet = false
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}