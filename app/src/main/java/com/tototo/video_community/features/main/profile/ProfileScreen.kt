package com.tototo.video_community.features.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tototo.video_community.AppState
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.compose.koinInject


@Composable
fun ProfileScreen() {
    val sharedViewModel = koinInject<SharedViewModel>()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Button(
            onClick = {
                sharedViewModel.updateAppState(AppState(isLogin = false))
            }
        ) {
            Text("退出登录")
        }
    }
}