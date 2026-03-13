package com.tototo.video_community.features.video

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.tototo.video_community.data.local.VideoQuality
import com.tototo.video_community.features.setting.SettingsViewModel
import com.tototo.video_community.ui.util.NetworkMonitor
import com.tototo.video_community.ui.util.NetworkType
import com.tototo.video_community.ui.util.SystemUiUtil
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    title: String,
    coverUrl: String,
    playUrl: String,
    desc: String,
    sources: Map<String, String>,
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val controller = remember { VideoPlayerController(context) }

    val wlanQuality = settingsViewModel.wlanQuality.collectAsState().value
    val mobileQuality = settingsViewModel.mobileQuality.collectAsState().value

    val networkMonitor = koinInject<NetworkMonitor>()
    val networkType = networkMonitor.networkType.collectAsState(initial = NetworkType.OTHER).value

    var showSheet by remember { mutableStateOf(false) }
    var selectedQuality by remember { mutableStateOf(VideoQuality.AUTO) }

    var isFullscreen by remember { mutableStateOf(false) }
    var controlsVisible by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }

    var durationMs by remember { mutableLongStateOf(0L) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var isUserDragging by remember { mutableStateOf(false) }

    var isBuffering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentUrl by remember { mutableStateOf(playUrl) }

    var seekHint by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
            }

            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlaying = isPlayingNow
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

    fun resolvePreferredQuality(): VideoQuality {
        val preferred = if (networkType == NetworkType.WIFI) wlanQuality else mobileQuality
        return if (preferred == VideoQuality.AUTO) VideoQuality.P720 else preferred
    }

    fun resolveUrlForQuality(q: VideoQuality): String {
        val key = q.code
        val fromMap = sources[key]
        return fromMap ?: playUrl
    }

    fun playByQuality(q: VideoQuality) {
        val realQ = if (q == VideoQuality.AUTO) resolvePreferredQuality() else q
        val url = resolveUrlForQuality(realQ)
        if (url.isBlank()) return
        currentUrl = url
        errorMessage = null
        controller.play(url)
    }

    LaunchedEffect(playUrl, sources, wlanQuality, mobileQuality, networkType) {
        if (playUrl.isBlank() && sources.isEmpty()) return@LaunchedEffect
        if (selectedQuality == VideoQuality.AUTO) {
            playByQuality(VideoQuality.AUTO)
            controlsVisible = true
        }
    }

    BackHandler(enabled = isFullscreen) {
        SystemUiUtil.exitFullScreen(activity)
        isFullscreen = false
        controlsVisible = true
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

    LaunchedEffect(isPlaying, isFullscreen, showSheet) {
        if (!isPlaying) return@LaunchedEffect
        if (showSheet) return@LaunchedEffect
        if (!controlsVisible) return@LaunchedEffect
        delay(2500)
        if (isPlaying && !showSheet) {
            controlsVisible = false
        }
    }

    LaunchedEffect(seekHint) {
        if (seekHint == null) return@LaunchedEffect
        delay(700)
        seekHint = null
    }

    if (title.isBlank() || (playUrl.isBlank() && sources.isEmpty())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                }
                Text("视频详情", style = MaterialTheme.typography.titleLarge)
            }
            Text("找不到该视频数据", style = MaterialTheme.typography.titleMedium)
            Button(onClick = onBack) { Text("返回") }
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = (if (isFullscreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(220.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { controlsVisible = !controlsVisible },
                        onDoubleTap = { offset: Offset ->
                            val width = size.width.toFloat().coerceAtLeast(1f)
                            val isLeft = offset.x < width / 2f
                            val delta = if (isLeft) -10_000L else 10_000L
                            val target = (controller.player.currentPosition + delta).coerceAtLeast(0L)
                            controller.player.seekTo(target)
                            seekHint = if (isLeft) "快退10秒" else "快进10秒"
                            controlsVisible = true
                        }
                    )
                }
        ) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = controller.player
                        useController = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (controlsVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }

            if (seekHint != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seekHint ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            if (errorMessage != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("错误：${errorMessage ?: ""}", color = Color.White)
                    Button(onClick = {
                        errorMessage = null
                        if (currentUrl.isNotBlank()) controller.play(currentUrl)
                        controlsVisible = true
                    }) { Text("重试播放") }
                }
            }

            if (isBuffering) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            if (controlsVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {
                            if (isFullscreen) {
                                SystemUiUtil.exitFullScreen(activity)
                                isFullscreen = false
                            } else {
                                onBack()
                            }
                            controlsVisible = true
                        }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回", tint = Color.White)
                        }
                        Text(
                            text = title,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )
                        IconButton(onClick = {
                            showSheet = true
                            controlsVisible = true
                        }) {
                            Icon(Icons.Rounded.Settings, contentDescription = "清晰度", tint = Color.White)
                        }
                        IconButton(onClick = {
                            if (isFullscreen) {
                                SystemUiUtil.exitFullScreen(activity)
                                isFullscreen = false
                            } else {
                                SystemUiUtil.enterFullScreen(activity)
                                isFullscreen = true
                            }
                            controlsVisible = true
                        }) {
                            Icon(
                                if (isFullscreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                                contentDescription = "全屏",
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                if (isPlaying) controller.pause() else controller.player.play()
                                controlsVisible = true
                            }) {
                                Icon(
                                    if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                    contentDescription = "播放暂停",
                                    tint = Color.White
                                )
                            }
                            Text("${formatMs(positionMs)} / ${formatMs(durationMs)}", color = Color.White)
                            Spacer(modifier = Modifier.height(1.dp))
                        }

                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                isUserDragging = true
                                sliderValue = it
                                controlsVisible = true
                            },
                            onValueChangeFinished = {
                                isUserDragging = false
                                if (durationMs > 0) {
                                    val target = (durationMs * sliderValue).toLong()
                                    controller.player.seekTo(target)
                                }
                                controlsVisible = true
                            }
                        )
                    }
                }
            }
        }

        if (!isFullscreen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Text(desc, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "网络：$networkType，默认画质：WLAN=${wlanQuality.label}，蜂窝=${mobileQuality.label}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showSheet) {
        val ordered = listOf(
            VideoQuality.AUTO,
            VideoQuality.P360,
            VideoQuality.P480,
            VideoQuality.P720,
            VideoQuality.P1080
        )

        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("选择清晰度", style = MaterialTheme.typography.titleMedium)

                ordered.forEach { q ->
                    val enabled = q == VideoQuality.AUTO || sources.containsKey(q.code) || playUrl.isNotBlank()
                    ListItem(
                        headlineContent = { Text(q.label) },
                        trailingContent = {
                            RadioButton(
                                selected = (selectedQuality == q),
                                onClick = {
                                    selectedQuality = q
                                    playByQuality(q)
                                    showSheet = false
                                    controlsVisible = true
                                },
                                enabled = enabled
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