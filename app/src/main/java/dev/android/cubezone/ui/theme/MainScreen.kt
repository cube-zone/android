package dev.android.cubezone.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.android.cubezone.R
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.runtime.remember
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import dev.android.cubezone.MainActivity
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.State
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState

val poppins = FontFamily(Font(resId = R.font.poppinsmedium))
val poppinsRegular = FontFamily(Font(resId = R.font.poppinsregular))
val poppinsSemiBold = FontFamily(Font(resId = R.font.poppinssemibold))

@Composable
fun MainScreen(
    solveState: SolveState,
    sessionState: SessionState,
    onSolveEvent: (SolveEvent) -> Unit,
    onSessionEvent: (SessionEvent) -> Unit,
    mainViewModel: MainViewModel,
    mainState: State
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        topBar = {Row(modifier = Modifier.padding(top = 5.dp)) {} },
        content = { paddingValues ->
            BottomNavGraph(
                navController = navController,
                paddingValues = paddingValues,
                solveState = solveState,
                onSolveEvent = onSolveEvent,
                sessionState = sessionState,
                onSessionEvent = onSessionEvent,
                viewModel = mainViewModel,
                mainState = mainState
            )
        }
    )
}
@Composable
fun Logo() {
    val view = LocalView.current
    val windowInsets = remember(view) { ViewCompat.getRootWindowInsets(view) }
    val insetTypes = WindowInsetsCompat.Type.systemBars()
    val insets = windowInsets?.getInsets(insetTypes)
    Log.d("DEBUG", insets.toString())
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
        Image(
            painter = painterResource(id = R.drawable.cube_zone_logo2_teal),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        /*
        Row (modifier = Modifier.padding(5.dp)){
            Text(
                text = "cube",
                fontSize = 16.sp,
                fontFamily = poppinsSemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "zone",
                fontSize = 16.sp,
                fontFamily = poppinsSemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
         */
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.stats,
        BottomBarScreen.timer,
        BottomBarScreen.solves
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar() {
        screens.forEach {screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem (
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {Text(text = screen.title)},
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = screen.icon),
                contentDescription = screen.title
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}