package dev.android.cubestudio.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.android.cubestudio.MainActivity
import dev.android.cubestudio.MainViewModel
import dev.android.cubestudio.databases.sessions.SessionEvent
import dev.android.cubestudio.databases.sessions.SessionState
import dev.android.cubestudio.databases.solves.SolveEvent
import dev.android.cubestudio.databases.solves.SolveState
import dev.android.cubestudio.screens.SolvesScreen
import dev.android.cubestudio.screens.StatsScreen
import dev.android.cubestudio.screens.TimerScreen

@Composable
fun BottomNavGraph(
    mainActivity: MainActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    solveState: SolveState,
    sessionState: SessionState,
    onSolveEvent: (SolveEvent) -> Unit,
    onSessionEvent: (SessionEvent) -> Unit,
    viewModel: MainViewModel

){
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.timer.route
    ) {
        composable(route = BottomBarScreen.timer.route) {
           TimerScreen(
               paddingValues = paddingValues,
               solveState = solveState,
               onSolveEvent = onSolveEvent,
               onSessionEvent = onSessionEvent,
               sessionState = sessionState,
               viewModel = viewModel
           )
        }
        composable(route = BottomBarScreen.stats.route) {
            StatsScreen()
        }
        composable(route = BottomBarScreen.solves.route) {
            SolvesScreen(
                solveState = solveState,
                paddingValues = paddingValues,
                onSessionEvent = onSessionEvent,
                sessionState = sessionState,
                viewModel = viewModel,
                onSolveEvent = onSolveEvent
            )
        }
    }
}