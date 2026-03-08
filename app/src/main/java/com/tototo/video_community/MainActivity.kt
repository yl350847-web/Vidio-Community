package com.tototo.video_community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.nav.AppNav
import com.tototo.video_community.ui.theme.VideoCommunityTheme // 👈 1. 确保导入你的自定义 Theme
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import com.tototo.video_community.ui.viewmodel.ThemeViewModel
import org.koin.compose.koinInject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sharedViewModel = koinInject<SharedViewModel>()
            val appState = sharedViewModel.appState.collectAsState().value

            val themeViewModel = koinViewModel<ThemeViewModel>()
            // 2. 收集主题状态 (Flow -> State)
            val isDark = themeViewModel.isDark.collectAsState().value

            // 3. 【核心修改】直接将 isDark 传给 VideoCommunityTheme
            // 当 isDark 变化时，Compose 会自动重组 VideoCommunityTheme，
            // 进而根据 darkTheme 参数选择 DarkColorScheme 或 LightColorScheme
            VideoCommunityTheme(
                darkTheme = isDark,       // 👈 显式控制深色模式
                dynamicColor = false      // 👈 建议先设为 false 测试纯色切换，确认无误后可改回 true
            ) {
                val navController = rememberNavController()
                AppNav(
                    appNavController = navController,
                    appState = appState
                )
            }
        }
    }
}