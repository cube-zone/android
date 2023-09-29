package dev.android.cubezone.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import dev.android.cubezone.R
import androidx.compose.runtime.setValue
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSessionDialog(
    state: SessionState,
    onEvent: (SessionEvent) -> Unit,
    scrambleType: String,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(SessionEvent.HideAddSessionDialog)
        },
        title = { Text(text = "Add session") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder = {
                    Text(text = "")
                }
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        onEvent(SessionEvent.SetSession(
                            sessionName = text,
                            scrambleType = scrambleType,
                            createdAt = System.currentTimeMillis(),
                            lastUsedAt = System.currentTimeMillis().toInt()
                        ))
                        onEvent(SessionEvent.SaveSession)
                        onEvent(SessionEvent.HideAddSessionDialog)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary),
                    )
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}