package dev.android.cubezone.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.databases.sessions.Session
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState

@Composable
fun SessionSelection(
    sessionState: SessionState,
    viewModel: MainViewModel,
    onSessionEvent: (SessionEvent) -> Unit,
    currentSession: Session?,
    currentScrambleType: String
) {
    var selectedSession by remember { mutableStateOf<Session?>(null) }
    val selectedSessionId = selectedSession?.sessionId

    // Initialize the selected session when the composable is first composed
    LaunchedEffect(currentSession, currentScrambleType) {
        selectedSession = sessionState.sessions.find { it.sessionId == currentSession?.sessionId }
    }

    var selectSessionButtonExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedButton(
            onClick = { selectSessionButtonExpanded = true },
            modifier = Modifier.animateContentSize().padding(5.dp, 0.dp),
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
                if (session.scrambleType == viewModel.state.currentScrambleType) {
                    DropdownMenuItem(
                        text = { Text(session.sessionName) },
                        onClick = {
                            selectedSession = session
                            selectSessionButtonExpanded = false
                            viewModel.updateCurrentSessionId(session.sessionId ?: 0)
                            Log.d(
                                "DEBUG",
                                "sessionId: ${viewModel.state.currentSessionId} target: ${session.sessionId}"
                            )
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
}