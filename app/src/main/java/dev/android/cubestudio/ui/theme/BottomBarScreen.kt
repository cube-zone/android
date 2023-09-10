package dev.android.cubestudio.ui.theme

import dev.android.cubestudio.R

sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: Int
){
    object stats: BottomBarScreen(
        route = "stats",
        title = "Stats",
        icon = R.drawable.outline_trending_up_24
    )
    object timer: BottomBarScreen(
        route = "timer",
        title = "Timer",
        icon = R.drawable.outline_timer_24
    )
    object solves: BottomBarScreen(
        route = "solves",
        title = "Solves",
        icon = R.drawable.baseline_format_list_bulleted_24
    )
}