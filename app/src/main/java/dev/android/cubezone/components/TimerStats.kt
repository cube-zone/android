package dev.android.cubezone.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.android.cubezone.databases.sessions.Session
import dev.android.cubezone.databases.solves.Solve
import dev.android.cubezone.screens.formatTime
import dev.android.cubezone.statistics.aoN

fun calculateAvg(range: Int, solves: List<Solve>, sessionId: Int): String? {
    var sum = 0L
    var size = 0
    if (solves.isNotEmpty() && solves.size > range) {
        var i = 0
        while (size < range) {
            if (solves[i].sessionId == sessionId) {
                sum += solves[i].time
                size++
            }
            if (i == solves.size - 1) {
                return null
            }
            i++
        }
    }
    return formatTime(sum / size.toFloat())
}
@Composable
fun TimerStats(
    sessionId: Int,
    solves: List<Solve>,
    modifier: Modifier
) {
    val sessionSolves by remember { mutableStateOf(solves.filter { it.sessionId == sessionId }) }
    Log.d("TimerStats", "sessionSolves: $sessionSolves" + "sessionId: $sessionId")
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 3.dp
        ) {
            Box(modifier = Modifier.width(IntrinsicSize.Max)) {
                Column {
                    Surface(
                        modifier = Modifier
                            .offset(0.dp, 10.dp)
                            .fillMaxWidth()
                            .height(20.dp),
                        tonalElevation = 5.dp
                    ) {}
                    Surface(
                        modifier = Modifier
                            .offset(0.dp, 30.dp)
                            .fillMaxWidth()
                            .height(20.dp),
                        tonalElevation = 5.dp
                    ) {}
                    Surface(
                        modifier = Modifier
                            .offset(0.dp, 50.dp)
                            .fillMaxWidth()
                            .height(20.dp),
                        tonalElevation = 5.dp
                    ) {}
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        "\n5\n12\n50\n100",
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Last Average\n${formatTime(aoN(5, sessionSolves.map {it.time})?.toFloat())}" +
                                "\n${formatTime(aoN(5, sessionSolves.map {it.time})?.toFloat())}" +
                                "\n${formatTime(aoN(5, sessionSolves.map {it.time})?.toFloat())}" +
                                "\n${formatTime(aoN(5, sessionSolves.map {it.time})?.toFloat())}",
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Best Average\n${calculateAvg(5, solves, sessionId = sessionId ) ?: "-"}" +
                                "\n${calculateAvg(12, solves, sessionId = sessionId ) ?: "-"}" +
                                "\n${calculateAvg(50, solves, sessionId = sessionId ) ?: "-"}" +
                                "\n${calculateAvg(100, solves, sessionId = sessionId ) ?: "-"}",
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}