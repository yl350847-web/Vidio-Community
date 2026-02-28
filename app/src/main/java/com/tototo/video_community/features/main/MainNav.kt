package com.tototo.video_community.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.features.main.navigation.MainRoute

@Composable
fun MainNav() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            MainNavHost(navController)
        }

        NavigationBar {
            NavigationBarItem(
                selected = currentRoute == MainRoute.Home,
                onClick = { navController.navigate(MainRoute.Home) },
                icon = { Icon(Icons.Rounded.Home, null) },
                label = { Text("首页") }
            )
            NavigationBarItem(
                selected = currentRoute == MainRoute.Subscription,
                onClick = { navController.navigate(MainRoute.Subscription) },
                icon = { Icon(Icons.Rounded.Subscriptions, null) },
                label = { Text("订阅") }
            )
            NavigationBarItem(
                selected = currentRoute == MainRoute.Profile,
                onClick = { navController.navigate(MainRoute.Profile) },
                icon = { Icon(Icons.Rounded.Person, null) },
                label = { Text("我的") }
            )
        }
    }
}

@Composable
private fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Home
    ) {
        composable(MainRoute.Home) { HomeScreen() }
        composable(MainRoute.Subscription) { SubscriptionScreen() }
        composable(MainRoute.Profile) { ProfileScreen() }
    }
}

@Composable
private fun HomeScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun SubscriptionScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Subscription", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun ProfileScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
    }
}