package com.example.eurdpayment.Navigation

import BottomNavigationSeparator
import PaymentScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eurdpayment.Algorand.assetList
import com.example.eurdpayment.Screens.HistoryScreen
import com.example.eurdpayment.Screens.ReceiveScreen
import com.example.eurdpayment.Screens.SendMoneyScreen

@Composable
fun Navigation(){

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.Gray, elevation = 0.dp, modifier = Modifier.padding(0.dp).height(57.dp)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEachIndexed { index, screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(screen.route, fontStyle = FontStyle.Normal, color = Color.White) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        modifier = Modifier.background(Color.Black),
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(0.4f),
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of th
                                // graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                    )
                    if (index < screens.size - 1) {
                        BottomNavigationSeparator()
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = EurdPaymentScreen.SendMoneyScreen.route, Modifier.padding(innerPadding)){
            composable(route = EurdPaymentScreen.SendMoneyScreen.route) {
                SendMoneyScreen(navController = navController)
            }
            composable(route = EurdPaymentScreen.ReceiveScreen.route) {
                ReceiveScreen(navController = navController)
            }
            composable(route = EurdPaymentScreen.HistoryScreen.route) {
                HistoryScreen(navController = navController)
            }
            composable(
                route = EurdPaymentScreen.PaymentScreen.route + "/{assetName}/{assetAmount}/{assetReceiver}",
                arguments = listOf(
                    navArgument("assetName") {
                        type = NavType.StringType
                    },
                    navArgument("assetAmount") { // Define the second argument
                        type = NavType.FloatType // Assuming the ID is a string, adjust if necessary
                    },
                    navArgument("assetReceiver") { // Define the second argument
                        type = NavType.StringType // Assuming the ID is a string, adjust if necessary
                    }
                )
            ) { entry ->

                PaymentScreen(
                    navController = navController,
                    asset = assetList.first { it.name ==  entry.arguments?.getString("assetName")},
                    amount = entry.arguments?.getFloat("assetAmount")!!,
                    receiver = entry.arguments?.getString("assetReceiver")!!
                )
            }
        }
    }
}