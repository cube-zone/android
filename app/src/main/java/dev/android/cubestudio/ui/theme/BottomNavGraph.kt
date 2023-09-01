package dev.android.cubestudio.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.android.cubestudio.MainActivity
import dev.android.cubestudio.databases.solves.SolveDao
import dev.android.cubestudio.databases.solves.SolveDatabase
import dev.android.cubestudio.databases.solves.SolveEvent
import dev.android.cubestudio.databases.solves.SolveState
import dev.android.cubestudio.databases.solves.SolveViewModel
import dev.android.cubestudio.screens.SolvesScreen
import dev.android.cubestudio.screens.StatsScreen
import dev.android.cubestudio.screens.TimerScreen

@Composable
fun BottomNavGraph(
    mainActivity: MainActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: SolveState,
    onEvent: (SolveEvent) -> Unit

){
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.timer.route
    ) {
        composable(route = BottomBarScreen.timer.route) {
           TimerScreen(
               paddingValues = paddingValues,
               state = state,
               onEvent = onEvent
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