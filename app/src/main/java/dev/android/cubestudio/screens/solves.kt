package dev.android.cubestudio.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import dev.android.cubestudio.components.SessionSelection
import dev.android.cubestudio.databases.solves.SolveState

@Composable
fun SolvesScreen(
    solveState: SolveState
) {
    Column {
        SessionSelection()
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
        ) {
            solveState.solves
        }
    }
}