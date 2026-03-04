package com.tototo.video_community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.nav.AppNav
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val sharedViewModel = koinInject<SharedViewModel>()
                val appState = sharedViewModel.appState.collectAsState().value
                val navController = rememberNavController()
                AppNav(
                    appNavController = navController,
                    appState = appState
                )
            }
        }
    }
}