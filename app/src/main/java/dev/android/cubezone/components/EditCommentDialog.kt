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
import dev.android.cubezone.databases.solves.Solve
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommentDialog(
    state: SolveState,
    onEvent: (SolveEvent) -> Unit,
    solve: Solve,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(SolveEvent.HideEditCommentDialog)
        },
        title = { Text(text = "Edit comment") },
        text = {
            OutlinedTextField(
                value = text ?: "",
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
                        onEvent(SolveEvent.EditComment(solve, text))
                        onEvent(SolveEvent.HideEditCommentDialog)},
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