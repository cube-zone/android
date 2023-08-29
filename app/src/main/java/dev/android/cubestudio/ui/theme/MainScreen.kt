package dev.android.cubestudio.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.android.cubestudio.R
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview

val poppins = FontFamily(Font(resId = R.font.poppinsmedium))
val poppinsSemiBold = FontFamily(Font(resId = R.font.poppinssemibold))

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        topBar = { Logo(name = "Cube Studio") },
        content = { paddingValues ->
            BottomNavGraph(
                navController = navController,
                paddingValues = paddingValues,
            )
        }
    )
    }
@Composable
fun Logo(name: String) {
    val view = LocalView.current
    val windowInsets = remember(view) { ViewCompat.getRootWindowInsets(view) }
    val insetTypes = WindowInsetsCompat.Type.systemBars()
    val insets = windowInsets?.getInsets(insetTypes)
    Log.d("DEBUG", insets.toString())
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp, (insets?.top!!/2+10).dp)) {
        Image(
            painter = painterResource(id = R.drawable.cubestudio_logo),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = name,
            fontSize = 16.sp,
            color = colorResource(R.color.text),
            modifier = Modifier.padding(10.dp, 0.dp),
            fontFamily = poppinsSemiBold
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.timer,
        BottomBarScreen.stats,
        BottomBarScreen.solves
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        containerColor = colorResource(id = R.color.secondaryBg),
        contentColor = colorResource(id = R.color.text),
    ) {
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
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colorResource(id = R.color.text),
            selectedTextColor = colorResource(id = R.color.text),
            unselectedIconColor = colorResource(id = R.color.secondaryText),
            unselectedTextColor = colorResource(id = R.color.secondaryText),
            indicatorColor = colorResource(id = R.color.primary)
        ),
        onClick = {
            navController.navigate(screen.route)
        }
    )
}