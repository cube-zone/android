package dev.android.cubezone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import dev.android.cubezone.R

@Composable
fun SolveOptions(
    plusTwo: () -> Unit,
    dnf: () -> Unit,
    deleteSolve: () -> Unit,
    addComment: () -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    showAddComment: Boolean = true
) {
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
        TextButton(onClick = deleteSolve) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }
    }
}