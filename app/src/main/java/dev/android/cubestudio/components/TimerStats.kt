package dev.android.cubestudio.components

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.android.cubestudio.databases.solves.Solve
import dev.android.cubestudio.screens.formatTime

fun calculateAvg(range: Int, solves: List<Solve>): String? {
    if (solves.size >= range) {
        var sum = 0L
        for (i in 0..range) {
            sum += solves[i].time
        }
        return formatTime(sum/range.toFloat())
    } else return null
}
@Composable
fun TimerStats(solves: List<Solve>, modifier: Modifier) {
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
                        "Last Avg\n${calculateAvg(5, solves) ?: "-"}" +
                                "\n${calculateAvg(12, solves) ?: "-"}" +
                                "\n${calculateAvg(50, solves) ?: "-"}" +
                                "\n${calculateAvg(100, solves) ?: "-"}",
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Best Avg\n${calculateAvg(5, solves) ?: "-"}" +
                                "\n${calculateAvg(12, solves) ?: "-"}" +
                                "\n${calculateAvg(50, solves) ?: "-"}" +
                                "\n${calculateAvg(100, solves) ?: "-"}",
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