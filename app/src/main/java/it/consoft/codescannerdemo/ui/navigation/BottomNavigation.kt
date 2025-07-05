package it.consoft.codescannerdemo.ui.navigation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationScreen(navController: NavController) {
    val items = listOf(
        NavItem.codeScanner,
        NavItem.codeGenerator,
        NavItem.codeRepository
    )

    NavigationBar(
        //Modifier.background(colorResource(id = R.color.teal_200)),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        //modifier = Modifier.size(40.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        //fontSize = 10.sp
                    )
                },

                /*selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),*/
                alwaysShowLabel = true,
                selected = currentRoute?.startsWith(item.screen_route) ?: false,
                onClick = {
                    Log.d("APPLICATION_CUSTOM", "CURRENT ROUTE: $currentRoute")
                    if (item is NavItem.codeGenerator) {
                        navController.navigate(NavItem.codeGenerator.screen_route) {
                            popUpTo(NavItem.codeGenerator.screen_route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        if(currentRoute?.contains(NavItem.codeGeneratorForm.screen_route) ?: false){
                            navController.popBackStack()
                        }
                        navController.navigate(item.screen_route) {
                            launchSingleTop = true
                        }
                    }
                    /*navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true,

                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }*/
                },
                /*colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        indicatorColor = SelectedItem
                    )*/
            )
        }
    }
}