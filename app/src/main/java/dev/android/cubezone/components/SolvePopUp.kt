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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.android.cubezone.databases.solves.Solve
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState
import dev.android.cubezone.screens.formatTime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun SolveOptionsNoComment(
    plusTwo: () -> Unit,
    dnf: () -> Unit,
    deleteSolve: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = plusTwo) {
            Text(
                "+2",
                fontSize = 18.sp
            )
        }
        TextButton(onClick = dnf) {
            Text(
                "DNF",
                fontSize = 18.sp
            )
        }
        TextButton(onClick = deleteSolve) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }
    }
}
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
    solve: Solve,
    onSolveEvent: (SolveEvent) -> Unit,
    solveState: SolveState
) {
    Log.d("DEBUG", "SolvePopupShown")
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = solve.createdAt
    AlertDialog(
        onDismissRequest = { onSolveEvent(SolveEvent.HideSolvePopup) }
    ) {
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
                            text = if (solve.dnf) "DNF" else {
                                formatTime(
                                    (solve.time + (solve.penalisation ?: 0)).toFloat()
                                )
                            },
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (solve.dnf) Color.Red else MaterialTheme.colorScheme.onSurface,
                        )
                        if (solve.penalisation != null && solve.penalisation != 0) {
                            Text(
                                text = " +${solve.penalisation / 1000}",
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Red,
                            )
                        }
                    }
                    Text(
                        text = getDateTime(solve.createdAt) ?: "Error",
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
                        text = solve.scramble,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                }
                Box(contentAlignment = Alignment.CenterEnd) {
                    Column {
                        HorizontalDivider()
                        if (solve.comment != null && solve.comment != "") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 10.dp, 54.dp, 10.dp)
                            ) {
                                Text(
                                    text = solve.comment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                    Box(modifier = Modifier.offset((-10).dp), contentAlignment = Alignment.Center){
                        TextButton(
                            onClick = { /*TODO*/ },
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
                SolveOptions(
                    plusTwo = { onSolveEvent(SolveEvent.PenaliseSolve(solve, 2000)) },
                    dnf = { onSolveEvent(SolveEvent.DnfSolve(solve, !solve.dnf )) },
                    deleteSolve = {
                        onSolveEvent(SolveEvent.DeleteSolve(solve)) 
                        onSolveEvent(SolveEvent.HideSolvePopup)
                    },
                    addComment = { onSolveEvent(SolveEvent.ShowEditCommentDialog) },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                )
            }
        }
    }
}