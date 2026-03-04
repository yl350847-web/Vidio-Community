package com.tototo.video_community.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tototo.video_community.AppState
import com.tototo.video_community.features.login.LoginNav
import com.tototo.video_community.features.main.MainNav
import com.tototo.video_community.features.search.SearchScreen
import com.tototo.video_community.features.splash.SplashScreen

@Composable
fun AppNav(
    appNavController: NavHostController,
    appState: AppState
) {
    var localIsLogin by remember { mutableStateOf(false) }

    LaunchedEffect(appState.isLogin) {
        localIsLogin = appState.isLogin
        if (localIsLogin) {
            appNavController.navigate(AppRoute.MainNav) {
                popUpTo(AppRoute.LoginNav) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = appNavController,
        startDestination = AppRoute.Splash,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(AppRoute.Splash) {
            SplashScreen {
                val nextRoute = AppRoute.MainNav
                appNavController.navigate(nextRoute) {
                    popUpTo(AppRoute.Splash) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable(AppRoute.LoginNav) { LoginNav() }
        composable(AppRoute.MainNav) {
            MainNav(
                appNavigateTo = { route ->
                    appNavController.navigate(route)
                }
            )
        }
        composable(
            route = "${AppRoute.Search}?q={q}",
            arguments = listOf(
                navArgument("q") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("q").orEmpty()
            SearchScreen(initialQuery = query)
        }
    }
}