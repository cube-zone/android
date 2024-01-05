package dev.android.cubezone.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.components.Statistic
import dev.android.cubezone.components.calculateAvg
import dev.android.cubezone.components.charts.ChartStyle
import dev.android.cubezone.components.charts.Line
import dev.android.cubezone.components.charts.LineChart
import dev.android.cubezone.components.charts.Point
import dev.android.cubezone.databases.sessions.Session
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState
import dev.android.cubezone.databases.solves.SolveState
import dev.android.cubezone.statistics.aoN
import dev.android.cubezone.statistics.bestSingleGraph
import dev.android.cubezone.statistics.pointsToTimes
import dev.android.cubezone.statistics.timesToPoints
import kotlin.math.ceil

@Composable
fun StatsScreen(
    solveState: SolveState,
    viewModel: MainViewModel,
    sessionState: SessionState,
    onSessionEvent: (SessionEvent) -> Unit,
    mainState: dev.android.cubezone.State,
    paddingValues: PaddingValues,
) {
    Column() {
        var selectedTabIndex by remember {
            mutableStateOf(0) }
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
            Tab(selected = true, onClick = { selectedTabIndex = 0 }) {
                Text(
                    text = "Session",
                    modifier = Modifier.padding(10.dp)
                )
            }
            Tab(selected = false, onClick = { selectedTabIndex = 1 }) {
                Text(text = "All Time")
            }
        }
        if (selectedTabIndex == 0) {
            SessionStats(
                solveState = solveState,
                viewModel = viewModel,
                sessionState = sessionState,
                onSessionEvent = onSessionEvent,
                mainState = mainState,
                paddingValues = paddingValues,
            )
        } else {
            GlobalStats()
        }
    }
}
@Composable
fun SessionStats(
    solveState: SolveState,
    viewModel: MainViewModel,
    sessionState: SessionState,
    onSessionEvent: (SessionEvent) -> Unit,
    mainState: dev.android.cubezone.State,
    paddingValues: PaddingValues,
) {
    var currentScramble: Current by remember { mutableStateOf(Current(mainState.currentScrambleType)) }
    var currentSessionId: Int by remember { mutableStateOf(mainState.currentSessionId) }
    var currentSession by remember { mutableStateOf<Session?>(null) }

    val averages by remember{ mutableStateOf(listOf(12, 50, 100, 500, 1000)) }

    if(currentScramble.type == null) currentScramble.type = mainState.currentScrambleType
    if(currentSession == null) currentSession = sessionState.sessions.find { it.sessionId == currentSessionId }
    viewModel.updateCurrentScrambleType(currentScramble.type ?: "3x3")
    viewModel.updateCurrentSessionId(currentSession?.sessionId ?: 0)
    Log.d("stats", "currentSession: ${currentSession?.sessionName}")
    var maxSolveTime by remember{ mutableStateOf(0f)}
    val solveData: MutableList<Point> by remember(currentSession) {
        maxSolveTime = 0f
        val points = mutableListOf<Point>()
        var i = 0
        solveState.solves.forEach { solve ->
            if (solve.sessionId == (currentSession?.sessionId ?: 0)) {
                if (solve.time > maxSolveTime) {
                    maxSolveTime = solve.time.toFloat()
                    Log.d("DEBUG", "new maxSolveTime: $maxSolveTime")
                }
                points += Point(i.toFloat(), solve.time.toFloat())
                i++
            }
        }
        mutableStateOf(points)
    }
    var ao5Data: MutableList<Point> by remember(currentSession) {
        val points = mutableListOf<Point>()
        var i = 0
        solveData.forEach { solve ->
            if (i < solveData.size - 4) {
                points += Point(i.toFloat(), aoN(5, solveData.map { it.y?.toLong() ?: 0 }.slice(i..i+4))?.toFloat() ?: 0f)
            }
            i++
        }
        mutableStateOf(points)
    }

    var bestSingleData: MutableList<Point> by remember(currentSession) {
        val times = pointsToTimes(solveData)
        if (times.isNotEmpty()) {
            mutableStateOf(timesToPoints(bestSingleGraph(times)))
        } else {
            mutableStateOf(mutableListOf())
        }
    }
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        item {
            Row(modifier = Modifier.padding(5.dp, 5.dp)) {
                var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    OutlinedButton(
                        onClick = { scrambleTypeButtonExpanded = true },
                        modifier = Modifier
                            .animateContentSize()
                            .padding(5.dp, 0.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentScramble.type ?: "3x3")
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                    DropdownMenu(
                        expanded = scrambleTypeButtonExpanded,
                        onDismissRequest = { scrambleTypeButtonExpanded = false },
                    ) {
                        scrambleNames.forEach { scrambleType ->
                            DropdownMenuItem(
                                text = { Text(scrambleType) },
                                onClick = {
                                    scrambleTypeButtonExpanded = false
                                    currentScramble.type = scrambleType
                                    viewModel.updateCurrentScramble("") //set to empty so it will be updated in timer
                                    if (sessionState.sessions.find { it.scrambleType == scrambleType } != null) {
                                        currentSession = sessionState.sessions.find { it.scrambleType == scrambleType }
                                        viewModel.updateCurrentScrambleType(scrambleType)
                                    } else { //if there are no sessions with this scramble type create a new one
                                        onSessionEvent(
                                            SessionEvent.SetSession(
                                                sessionName = "default",
                                                scrambleType = scrambleType,
                                                createdAt = System.currentTimeMillis(),
                                                lastUsedAt = System.currentTimeMillis().toInt()
                                            )
                                        )
                                        onSessionEvent(SessionEvent.SaveSession)
                                        currentSession = sessionState.sessions.find { it.scrambleType == scrambleType }
                                        Log.d("DEBUG", "currentSession: ${currentSession?.sessionName}")
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 15.dp),
                                modifier = Modifier.sizeIn(maxHeight = 35.dp)
                            )
                        }
                    }
                }
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
                            Text(currentSession?.sessionName ?: "Not Selected")
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
                                        currentSession = session
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
        }
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 5.dp, 10.dp, 0.dp),
                tonalElevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
            ) {
                if (solveData.isNotEmpty()) {
                    Log.d("DEBUG", "maxSolveTime: $maxSolveTime")
                    Log.d("DEBUG", "niceNumber: ${ceilToNiceNumber(maxSolveTime.toLong())}")
                    LineChart(
                        data = solveData,
                        style = ChartStyle(
                            lineColor = MaterialTheme.colorScheme.primary,
                            showHorizontalGridLines = true,
                            gridLinesColor = Color.Gray,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp)
                            .height(200.dp),
                        lines = listOf(
                            Line(
                                points = bestSingleData,
                                color = Color.Yellow,
                                thickness = 2f,
                            ),
                            Line(
                                points = ao5Data,
                                color = Color.Red,
                                thickness = 2f,
                            ),
                            Line(
                                points = solveData,
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 5f,
                            ),
                        ),
                        yRange = ceilToNiceNumber(maxSolveTime.toLong()).toFloat(),
                        ySteps = 5
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
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
        item {
            Column(
                modifier = Modifier.padding(5.dp, 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(0.dp, 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Statistic(
                        label = "Pb",
                        value = "25.34",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(5.dp, 0.dp),
                        //imageVector = Icons.Outlined.Star,
                        color = Color.Yellow,
                        //secondaryColor = Color.Yellow
                    )
                    Statistic(
                        label = "Time Spent",
                        value = "1d 2h",
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(5.dp, 0.dp),
                        //painter = painterResource(id = R.drawable.outline_timer_24),
                        color = MaterialTheme.colorScheme.primary,
                        //secondaryColor = Color.Cyan
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp, 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Statistic(label = "Number of Solves",
                        value = "2561",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(5.dp, 0.dp)
                    )
                    Statistic(
                        label = "All time average",
                        value = "36.34",
                        modifier = Modifier
                            .padding(5.dp, 0.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
        item {
            Surface (
                tonalElevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(10.dp, 0.dp, 10.dp, 10.dp)
            ) {
                Box {
                    for (i in 0..averages.size+1 step 2) {
                        Surface (
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 25.dp * i + 10.dp)
                                .height(25.dp),
                            tonalElevation = 3.dp,
                        ) {}
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        var i by remember { mutableStateOf(0)}
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AverageText(text = "")
                            for (average in averages) {
                                AverageText(text = average.toString())
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AverageText(text = "Average")
                            for (average in averages) {
                                val avg = calculateAvg(average, solveState.solves, sessionId = currentSession?.sessionId ?: 0)
                                val text = if (avg != null && avg != "0.00") avg else "-"
                                AverageText(text = text)
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AverageText(text = "Deviation")
                            for (average in averages) {
                                //TODO: calculate deviation
                                val avg = calculateAvg(average, solveState.solves, sessionId = currentSession?.sessionId ?: 0)
                                val text = if (avg != null && avg != "0.00") avg else "-"
                                AverageText(text = text)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AverageText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(0.dp, 2.5.dp)
    )
}
fun ceilToNiceNumber(milliseconds: Long): Long {
    return when (milliseconds) {
        in 0..1000 -> 1000                                                                    //(0 - 1s) -> always 1s
        in 1_000..10_000 -> (ceil(milliseconds / 1000.0) * 1000).toLong()                  //(1s - 10s) -> 1s
        in 10_000..30_000 -> (ceil(milliseconds / 2000.0) * 2000).toLong()                 //(10s - 30s) -> 2s
        in 30_000..60_000 -> (ceil(milliseconds / 5000.0) * 5000).toLong()                 //(30s - 1m) -> 5s
        in 60_000..600_000 -> (ceil(milliseconds / 10_000.0) * 10_000).toLong()            //(1m - 10m) -> 10s
        in 600_000..3_600_000 -> (ceil(milliseconds / 60_000.0) * 60_000).toLong()         //(10m - 1h) -> 1m
        else -> (ceil(milliseconds / 300_000.0) * 300_000).toLong()                              //(1h+) -> 5m
    }
}