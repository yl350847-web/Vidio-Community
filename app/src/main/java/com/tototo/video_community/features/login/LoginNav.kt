package com.tototo.video_community.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tototo.video_community.AppState
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.compose.koinInject

@Composable
fun LoginNav() {
    val sharedViewModel = koinInject<SharedViewModel>()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login")
        Button(
            onClick = {
                sharedViewModel.updateAppState(AppState(isLogin = true))
            }
        ) {
            Text("模拟登录")
        }
    }
}