package dev.android.cubestudio.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.compose.CubeStudioTheme
import dev.android.cubestudio.MainViewModel
import dev.android.cubestudio.components.ScrambleSelection
import dev.android.cubestudio.components.SessionSelection
import dev.android.cubestudio.databases.sessions.Session
import dev.android.cubestudio.databases.sessions.SessionEvent
import dev.android.cubestudio.databases.sessions.SessionState
import dev.android.cubestudio.databases.solves.SolveState
import java.util.concurrent.TimeUnit

fun formatDuration(milliseconds: Long): String {
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
    val years = days / 365 // Approximate, not considering leap years

    return when {
        years > 0 -> "$years ${if (years == 1L) "year" else "yrs"}"
        days > 0 -> "$days ${if (days == 1L) "day" else "days"}"
        hours > 0 -> "$hours ${if (hours == 1L) "hour" else "hrs"}"
        minutes > 0 -> "$minutes ${if (minutes == 1L) "min" else "mins"}"
        seconds > 0 -> "$seconds ${if (seconds == 1L) "sec" else "secs"}"
        else -> "now"
    }
}

@Composable
fun SolvesScreen(
    solveState: SolveState,
    sessionState: SessionState,
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    onSessionEvent: (SessionEvent) -> Unit
) {
    var currentScrambleType: Current by remember { mutableStateOf(Current(viewModel.state.currentScrambleType)) }
    var currentSession: Session? by remember { mutableStateOf(null) }
    currentSession = sessionState.sessions.find { it.sessionId == viewModel.state.currentSessionId }
    Column(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        Row(modifier = Modifier.padding(5.dp, 0.dp)) {
            ScrambleSelection(viewModel = viewModel, currentScramble = currentScrambleType)
            SessionSelection(
                viewModel = viewModel,
                currentScrambleType = viewModel.state.currentScrambleType,
                currentSession = sessionState.sessions.find { it.sessionId == viewModel.state.currentSessionId },
                onSessionEvent = onSessionEvent,
                sessionState = sessionState
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 20.dp),
            tonalElevation = 3.dp,
            shape = MaterialTheme.shapes.large
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.Start,
                state = rememberLazyListState(),
                modifier = Modifier
            ) {
                solveState.solves.forEach() { solve -> //TODO Make this a list of solves from the selected session
                    item {
                        Box {
                            if (solveState.solves.indexOf(solve) % 2 != 0) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                                        .height(40.dp),
                                    tonalElevation = 6.dp,
                                ) {}
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .heightIn(max = 40.dp)
                                    .fillMaxWidth()
                                    .padding(0.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (solve.dnf) "DNF" else {
                                                formatTime(
                                                    (solve.time + (solve.penalisation ?: 0)).toFloat()
                                                )
                                            },
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (solve.dnf) Color.Red else Color.White,
                                            lineHeight = 20.sp
                                        )
                                        if (solve.penalisation != null && solve.penalisation != 0) {
                                            Text(
                                                text = " +${solve.penalisation / 1000}",
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Red,
                                            )
                                        }
                                    }
                                    if (solve.wasPb && !solve.dnf) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = Color.Yellow,
                                            modifier = Modifier.padding(0.dp)
                                        )
                                    }
                                }

                                if (solve.comment != null && solve.comment != "") {
                                    Text(
                                        text = solve.comment ?: "",
                                        modifier = Modifier.padding(10.dp),
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                                Text(
                                    text = formatDuration(System.currentTimeMillis() - solve.createdAt),
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}