package dev.android.cubezone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.android.cubezone.R
import dev.android.cubezone.databases.solves.Solve

@Composable
fun SolveOptions(
    plusTwo: () -> Unit,
    dnf: () -> Unit,
    deleteSolve: () -> Unit,
    addComment: () -> Unit,
    lastSolve: Solve? = null,
    showAddComment: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    if (showConfirmDeleteDialog && lastSolve != null) {
        Dialog(onDismissRequest = {showConfirmDeleteDialog = false}) {
            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    Modifier.padding(10.dp).width(250.dp)
                ) {
                    Text(text = "Delete solve?", modifier = Modifier.padding(10.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = { showConfirmDeleteDialog = false },
                        ) {
                            Text(text = "Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        TextButton(
                            onClick = {
                                showConfirmDeleteDialog = false
                                deleteSolve()
                            },
                        ) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showAddComment) {
            TextButton(onClick = addComment) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_comment_24),
                    contentDescription = null,
                )
            }
        }
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
        TextButton(onClick = { showConfirmDeleteDialog = true }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }
    }
}