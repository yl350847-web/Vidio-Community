package com.tototo.video_community.features.main

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tototo.video_community.features.main.home.HomeScreen
import com.tototo.video_community.features.main.navigation.MainRoute
import com.tototo.video_community.features.main.profile.ProfileScreen
import com.tototo.video_community.features.main.subscription.SubscriptionScreen
import com.tototo.video_community.nav.AppRoute

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun MainNav(
    appNavigateTo: (String) -> Unit
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(MainRoute.Home, "首页", Icons.Rounded.Home),
        BottomNavItem(MainRoute.Subscription, "订阅", Icons.Rounded.Subscriptions),
        BottomNavItem(MainRoute.Profile, "我的", Icons.Rounded.Person)
    )

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val width = configuration.screenWidthDp
    val isDesktop = width >= 840
    val isTablet = width in 600..839

    val onSearchClick = { appNavigateTo(AppRoute.Search) }
    val onNavigateToSearchWithQuery: (String) -> Unit = { q ->
        val route = "${AppRoute.Search}?q=${Uri.encode(q)}"
        appNavigateTo(route)
    }

    when {
        isDesktop || (!isPortrait && isTablet) -> {
            DesktopOrTabletContent(
                navController = navController,
                items = items,
                currentRoute = currentRoute,
                onSearchClick = onSearchClick,
                onNavigate = { route -> navController.navigate(route) },
                onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
            )
        }
        isPortrait -> {
            PortraitContent(
                navController = navController,
                items = items,
                currentRoute = currentRoute,
                onSearchClick = onSearchClick,
                onNavigate = { route -> navController.navigate(route) },
                onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
            )
        }
        else -> {
            LandscapeContent(
                navController = navController,
                items = items,
                currentRoute = currentRoute,
                onSearchClick = onSearchClick,
                onNavigate = { route -> navController.navigate(route) },
                onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
            )
        }
    }
}

@Composable
private fun PortraitContent(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    onSearchClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onNavigateToSearchWithQuery: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(onSearchClick)
        Box(Modifier.weight(1f)) {
            MainNavHost(
                navController = navController,
                onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
            )
        }
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    icon = { Icon(item.icon, null) },
                    label = { Text(item.label) }
                )
            }
        }
    }
}

@Composable
private fun LandscapeContent(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    onSearchClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onNavigateToSearchWithQuery: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(onSearchClick)
        Row(Modifier.weight(1f)) {
            SideNavigateRail(items, currentRoute, onNavigate)
            Box(Modifier.fillMaxSize()) {
                MainNavHost(
                    navController = navController,
                    onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
                )
            }
        }
    }
}

@Composable
private fun DesktopOrTabletContent(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    onSearchClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onNavigateToSearchWithQuery: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(onSearchClick)
        Row(Modifier.weight(1f)) {
            SideNavigateRail(items, currentRoute, onNavigate)
            Box(Modifier.fillMaxSize()) {
                MainNavHost(
                    navController = navController,
                    onNavigateToSearchWithQuery = onNavigateToSearchWithQuery
                )
            }
        }
    }
}

@Composable
private fun SideNavigateRail(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationRail {
        items.forEach { item ->
            NavigationRailItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, null) },
                label = { Text(item.label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onSearchClick: () -> Unit) {
    TopAppBar(
        title = { Text("BiliTube") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Rounded.Search, contentDescription = null)
            }
        }
    )
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    onNavigateToSearchWithQuery: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Home
    ) {
        composable(MainRoute.Home) {
            HomeScreen(
                onNavigateToSearch = onNavigateToSearchWithQuery
            )
        }
        composable(MainRoute.Subscription) {
            SubscriptionScreen()
        }
        composable(MainRoute.Profile) {
            ProfileScreen()
        }
    }
}