package dev.android.cubezone.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.databases.solves.Solve
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState
import dev.android.cubezone.screens.formatTime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private fun getDateTime(s: Long): String? {
    return try {
        val sdf = SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
        val netDate = Date(s)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolvePopUp(
    solveId: Int,
    comment: String?,
    onSolveEvent: (SolveEvent) -> Unit,
    solveState: SolveState,
    viewModel: MainViewModel
) {
    var currentSolve by remember{ mutableStateOf(solveState.solves.find { it.solveId == solveId }!!) }
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = currentSolve.createdAt
    AlertDialog(
        onDismissRequest = { onSolveEvent(SolveEvent.HideSolvePopup) }
    ) {
        if(solveState.isEditingComment) {
            currentSolve = solveState.solves.find { it.solveId == solveId }!!
            EditCommentDialog(
                solve = currentSolve,
                state = solveState,
                onEvent = onSolveEvent,
            )
            Log.d("DEBUG", "SolvePopUp: ${solveState.solves.find { it.solveId == solveId }!!}")
            currentSolve = solveState.solves.find { it.solveId == solveId }!!
        }
        Log.d("DEBUG", "SolvePopUp: ${solveState.solves.find { it.solveId == solveId }!!}")
        currentSolve = solveState.solves.find { it.solveId == solveId }!!
        Surface(
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(10.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (currentSolve.dnf) "DNF" else {
                                formatTime(
                                    (currentSolve.time + (currentSolve.penalisation ?: 0)).toFloat()
                                )
                            },
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (currentSolve.dnf) Color.Red else MaterialTheme.colorScheme.onSurface,
                        )
                        if (currentSolve.penalisation != null && currentSolve.penalisation != 0) {
                            Text(
                                text = " +${currentSolve.penalisation!! / 1000}",
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Red,
                            )
                        }
                    }
                    Text(
                        text = getDateTime(currentSolve.createdAt) ?: "Error",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = currentSolve.scramble,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                }
                if (currentSolve.comment != null && currentSolve.comment != "") {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        Column {
                            HorizontalDivider()
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 10.dp, 54.dp, 10.dp)
                            ) {
                                Text(
                                    text = currentSolve.comment ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.offset((-10).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(
                                onClick = { onSolveEvent(SolveEvent.ShowEditCommentDialog) },
                                modifier = Modifier.size(34.dp)
                            ) {}
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                            )
                        }
                    }
                }
                HorizontalDivider()
                SolveOptions(
                    plusTwo = {
                        onSolveEvent(SolveEvent.PenaliseSolve(currentSolve, (currentSolve.penalisation ?: 0) + 2000, false))
                        onSolveEvent(SolveEvent.HideSolvePopup)
                    },
                    dnf = {
                        onSolveEvent(SolveEvent.PenaliseSolve(currentSolve, 0, !currentSolve.dnf))
                        onSolveEvent(SolveEvent.HideSolvePopup)
                    },
                    deleteSolve = {
                        onSolveEvent(SolveEvent.DeleteSolve(currentSolve))
                        onSolveEvent(SolveEvent.HideSolvePopup)
                    },
                    addComment = { onSolveEvent(SolveEvent.ShowEditCommentDialog) },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    showAddComment = (currentSolve.comment == null || currentSolve.comment == ""),
                )
            }
        }
    }
}