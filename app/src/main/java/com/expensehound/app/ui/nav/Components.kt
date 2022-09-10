package com.expensehound.app.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_half

@Composable
fun DemoBottomNavigation(
    navController: NavHostController, items: List<AppScreens>
) {
    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val route = navBackStackEntry?.destination
        items.forEach { screen ->
            val isSelected: Boolean = route?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(icon = {
                Icon(
                    screen.icon, contentDescription = screen.resourceId.toString()
                )
            },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = isSelected,
                alwaysShowLabel = false,
                onClick = {
                    // If on current screen, ignore button press to avoid redraw
                    if (screen.route != route?.route) {
                        navController.navigate(screen.route)
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoTopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route
    SmallTopAppBar(
        navigationIcon = {
            if (route.equals("list") || route.equals("future") || route.equals("stats")) {
                val iconToSet = when (route) {
                    "list" -> AppScreens.HomeNav.icon
                    "future" -> AppScreens.Future.icon
                    "stats" -> AppScreens.Stats.icon
                    else -> AppScreens.Detail.icon
                }
                Icon(
                    imageVector = iconToSet,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = margin_half)
                )
            }
        },
        title = {
            if (route.equals("list")) {
                Text(stringResource(id = R.string.nav_home))
            }
            if (route.equals("future")) {
                Text(stringResource(id = R.string.nav_nava))
            }
            if (route.equals("stats")) {
                Text(stringResource(id = R.string.stats))
            }
        },

        // Material design suggests avoiding large areas of bright colors in dark theme. A common
        // pattern is to color a container primary color in light theme and surface color in dark
        // themes; many components use this strategy by default e.g. App Bars and Bottom Navigation.
        // To make this easier to implement, Colors offers a primarySurface color which provides
        // exactly this behaviour and these components use by default.
//        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoAddNewPurchaseTopAppBar(onDismiss: () -> Unit, onSave: () -> Unit) {
    SmallTopAppBar(title = { Text(stringResource(R.string.enter_a_color)) }, navigationIcon = {
        IconButton(onClick = { onDismiss() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                contentDescription = ".",
            )
        }
    }, actions = {
        TextButton(onClick = { onSave() }) {
            Text("Запази")
        }
    })
}