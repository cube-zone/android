package dev.android.cubezone.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.State
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState
import dev.android.cubezone.screens.SolvesScreen
import dev.android.cubezone.screens.StatsScreen
import dev.android.cubezone.screens.TimerScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    solveState: SolveState,
    sessionState: SessionState,
    onSolveEvent: (SolveEvent) -> Unit,
    onSessionEvent: (SessionEvent) -> Unit,
    viewModel: MainViewModel,
    mainState: State,
){
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.timer.route,
    ) {
        composable(route = BottomBarScreen.timer.route) {
           TimerScreen(
               paddingValues = paddingValues,
               solveState = solveState,
               onSolveEvent = onSolveEvent,
               onSessionEvent = onSessionEvent,
               sessionState = sessionState,
               viewModel = viewModel,
               mainState = mainState,
           )
        }
        composable(route = BottomBarScreen.stats.route) {
            StatsScreen(
                solveState = solveState,
                viewModel = viewModel,
                sessionState = sessionState,
                onSessionEvent = onSessionEvent,
                mainState = mainState,
                paddingValues = paddingValues,
            )
        }
        composable(route = BottomBarScreen.solves.route) {
            SolvesScreen(
                solveState = solveState,
                paddingValues = paddingValues,
                onSessionEvent = onSessionEvent,
                sessionState = sessionState,
                viewModel = viewModel,
                onSolveEvent = onSolveEvent,
                mainState = mainState,
            )
        }
    }
}