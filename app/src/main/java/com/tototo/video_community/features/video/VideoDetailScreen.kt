package com.tototo.video_community.features.video

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.tototo.video_community.data.local.VideoQuality
import com.tototo.video_community.features.setting.SettingsViewModel
import com.tototo.video_community.ui.util.SystemUiUtil
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    title: String,
    coverUrl: String,
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val controller = remember { VideoPlayerController(context) }

    val wlanQuality = settingsViewModel.wlanQuality.collectAsState().value
    val mobileQuality = settingsViewModel.mobileQuality.collectAsState().value

    val sources = remember { VideoQualitySource.sources() }

    var showSheet by remember { mutableStateOf(false) }
    var selectedQuality by remember { mutableStateOf(VideoQuality.P720) }
    var isFullscreen by remember { mutableStateOf(false) }

    var durationMs by remember { mutableLongStateOf(0L) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var isUserDragging by remember { mutableStateOf(false) }

    var isBuffering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentUrl by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
            }

            override fun onPlayerError(error: PlaybackException) {
                errorMessage = error.message ?: "播放出错"
            }
        }
        controller.player.addListener(listener)

        onDispose {
            controller.player.removeListener(listener)
            if (isFullscreen) {
                SystemUiUtil.exitFullScreen(activity)
            }
            controller.release()
        }
    }

    LaunchedEffect(wlanQuality) {
        val q = if (wlanQuality == VideoQuality.AUTO) VideoQuality.P720 else wlanQuality
        selectedQuality = q
        val url = sources.firstOrNull { it.quality == q }?.url ?: sources.first().url
        currentUrl = url
        errorMessage = null
        controller.play(url)
    }

    BackHandler(enabled = isFullscreen) {
        SystemUiUtil.exitFullScreen(activity)
        isFullscreen = false
    }

    LaunchedEffect(controller.player) {
        while (true) {
            val d = controller.player.duration
            durationMs = if (d > 0) d else 0L
            positionMs = controller.player.currentPosition.coerceAtLeast(0L)
            if (!isUserDragging) {
                sliderValue = if (durationMs > 0) {
                    (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                } else 0f
            }
            delay(500)
        }
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
                if (isBuffering) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator()
                        Text("缓冲中…", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (errorMessage != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("错误：${errorMessage ?: ""}")
                        Button(onClick = {
                            errorMessage = null
                            if (currentUrl.isNotBlank()) controller.play(currentUrl)
                        }) {
                            Text("重试播放")
                        }
                    }
                }

                Text(
                    "默认画质：WLAN=${wlanQuality.label}，蜂窝=${mobileQuality.label}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Slider(
                        value = sliderValue,
                        onValueChange = {
                            isUserDragging = true
                            sliderValue = it
                        },
                        onValueChangeFinished = {
                            isUserDragging = false
                            if (durationMs > 0) {
                                val target = (durationMs * sliderValue).toLong()
                                controller.player.seekTo(target)
                            }
                        }
                    )
                    Text(
                        "进度：${formatMs(positionMs)} / ${formatMs(durationMs)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { showSheet = true }) {
                        Text("清晰度：${selectedQuality.label}")
                    }
                    Button(onClick = {
                        SystemUiUtil.enterFullScreen(activity)
                        isFullscreen = true
                    }) {
                        Text("全屏")
                    }
                    Button(onClick = { controller.pause() }) {
                        Text("暂停")
                    }
                }
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
                                    currentUrl = src.url
                                    errorMessage = null
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

private fun formatMs(ms: Long): String {
    val totalSec = ms / 1000
    val m = totalSec / 60
    val s = totalSec % 60
    return "%02d:%02d".format(m, s)
}