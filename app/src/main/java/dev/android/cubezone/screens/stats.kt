package dev.android.cubezone.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.components.ScrambleSelection
import dev.android.cubezone.components.charts.ChartStyle
import dev.android.cubezone.components.charts.Point
import dev.android.cubezone.databases.sessions.Session
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState
import dev.android.cubezone.databases.solves.SolveState

@Composable
fun StatsScreen(
    solveState: SolveState,
    viewModel: MainViewModel,
    sessionState: SessionState,
    onSessionEvent: (SessionEvent) -> Unit,
    mainState: dev.android.cubezone.State,
) {
    Log.d("DEBUG", "Outside remember - currentSessionId: ${mainState.currentSessionId}")
    var currentScrambleType: Current by remember { mutableStateOf(Current(mainState.currentScrambleType)) }
    var currentSessionId: Int by remember { mutableStateOf(mainState.currentSessionId) }

    var selectedSession by remember { mutableStateOf<Session?>(null) }
    LaunchedEffect(sessionState.sessions.find { it.sessionId == mainState.currentSessionId }) {
        selectedSession = sessionState.sessions.find { it.sessionId == sessionState.sessions.find { it.sessionId == mainState.currentSessionId }?.sessionId }
    }
    var maxSolveTime = 0f
    var solveData: MutableList<Point> by remember(selectedSession) {
        val points = mutableListOf<Point>()
        var i = 0
        solveState.solves.forEach { solve ->
            if (solve.time > maxSolveTime) { maxSolveTime = solve.time.toFloat() }
            if (solve.sessionId == (selectedSession?.sessionId ?: 0)) {
                points += Point(i.toFloat(), solve.time.toFloat())
                i++
            }
        }
        mutableStateOf(points)
    }
    Column {
        Row(modifier = Modifier.padding(5.dp, 0.dp)) {
            ScrambleSelection(viewModel = viewModel, currentScramble = currentScrambleType)
            var selectSessionButtonExpanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                OutlinedButton(
                    onClick = { selectSessionButtonExpanded = true },
                    modifier = Modifier
                        .animateContentSize()
                        .padding(5.dp, 0.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(selectedSession?.sessionName ?: "New Session")
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                }
                DropdownMenu(
                    expanded = selectSessionButtonExpanded,
                    onDismissRequest = { selectSessionButtonExpanded = false },
                ) {
                    sessionState.sessions.forEach { session ->
                        if (session.scrambleType == mainState.currentScrambleType) {
                            DropdownMenuItem(
                                text = { Text(session.sessionName) },
                                onClick = {
                                    selectedSession = session
                                    selectSessionButtonExpanded = false
                                    viewModel.updateCurrentSessionId(session.sessionId ?: 0)
                                },
                                contentPadding = PaddingValues(horizontal = 15.dp),
                                modifier = Modifier.sizeIn(maxHeight = 35.dp)
                            )
                        }
                    }
                    DropdownMenuItem(
                        text = { Text("New Session") },
                        trailingIcon = { Icon(Icons.Default.Add, null) },
                        onClick = {
                            onSessionEvent(SessionEvent.ShowAddSessionDialog)
                            selectSessionButtonExpanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        modifier = Modifier.sizeIn(maxHeight = 35.dp)
                    )
                }
            }
            currentSessionId = mainState.currentSessionId
        }
        if (solveData.isNotEmpty()) {
            dev.android.cubezone.components.charts.LineChart(
                data = solveData,
                style = ChartStyle(
                    lineColor = MaterialTheme.colorScheme.primary,
                    lineThickness = 5f,
                    showVerticalGridLines = true,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(300.dp),
            )
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = "No solves yet",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}