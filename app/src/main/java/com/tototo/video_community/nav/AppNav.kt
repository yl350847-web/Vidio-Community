package com.tototo.video_community.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.AppState

@Composable
fun AppNav(appState: AppState) {
    val navController = rememberNavController()
    val start = if (appState.isLogin) AppRoute.Main else AppRoute.Login

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable(AppRoute.Login) {
            LoginScreen()
        }
        composable(AppRoute.Main) {
            MainScreen()
        }
    }
}

@Composable
private fun LoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Login Screen")
    }
}

@Composable
private fun MainScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Main Screen")
    }
}