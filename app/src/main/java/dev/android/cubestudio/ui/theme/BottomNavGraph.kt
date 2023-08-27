package dev.android.cubestudio.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.android.cubestudio.screens.SolvesScreen
import dev.android.cubestudio.screens.StatsScreen
import dev.android.cubestudio.screens.TimerScreen

@Composable
fun BottomNavGraph(navController: NavHostController, modifier: Modifier = Modifier, paddingValues: PaddingValues){
    var scrambleType by remember { mutableStateOf("3x3") }
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.timer.route
    ) {
        composable(route = BottomBarScreen.timer.route) {
           TimerScreen(
               scrambleType = scrambleType, session = "Default",
               paddingValues = paddingValues
           )
        }
        composable(route = BottomBarScreen.stats.route) {
            StatsScreen()
        }
        composable(route = BottomBarScreen.solves.route) {
            SolvesScreen()
        }
    }
}