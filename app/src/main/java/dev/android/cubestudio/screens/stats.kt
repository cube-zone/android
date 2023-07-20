package dev.android.cubestudio.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatsScreen() {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(color = Color.Blue, modifier = Modifier.fillMaxSize()){
        }
        Text(text = "Stats", textAlign = TextAlign.Center)
    }
}