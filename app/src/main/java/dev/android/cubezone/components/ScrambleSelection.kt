package dev.android.cubezone.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.screens.Current
import dev.android.cubezone.screens.scrambleNames

@Composable
fun ScrambleSelection(
    viewModel: MainViewModel,
    currentScramble: Current
) {
    var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedButton(
            onClick = { scrambleTypeButtonExpanded = true },
            modifier = Modifier.animateContentSize().padding(5.dp, 0.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentScramble.Type)
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
                        currentScramble.Type = scrambleType
                        viewModel.updateCurrentScrambleType(scrambleType)
                    },
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    modifier = Modifier.sizeIn(maxHeight = 35.dp)
                )
            }
        }
    }
}