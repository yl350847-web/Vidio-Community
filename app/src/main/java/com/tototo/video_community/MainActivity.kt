package com.tototo.video_community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.nav.AppNav
import com.tototo.video_community.ui.theme.VideoCommunityTheme
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import com.tototo.video_community.ui.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sharedViewModel = koinInject<SharedViewModel>()
            val appState = sharedViewModel.appState.collectAsState().value

            val themeViewModel = koinViewModel<ThemeViewModel>()
            val isDark = themeViewModel.isDark.collectAsState().value
            val dynamicColor = themeViewModel.isDynamicColor.collectAsState().value

            VideoCommunityTheme(
                darkTheme = isDark,
                dynamicColor = dynamicColor
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