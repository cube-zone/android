package dev.android.cubezone.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.android.cubezone.components.Statistic

@Composable
fun GlobalStats() {
    Column(
        modifier = Modifier.padding(5.dp, 15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp, 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Statistic(
                label = "Total Solves", value = "3504",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(5.dp, 0.dp),
            )
            Statistic(
                label = "Time spent solving",
                value = "2d 3h 15m",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp, 0.dp),
            )
        }
        Row(
            modifier = Modifier
                .padding(0.dp, 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Statistic(
                label = "Favorite event",
                value = "3x3",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(5.dp, 0.dp),
            )
            Statistic(
                label = "Number of sessions",
                value = "15",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp, 0.dp),
            )
        }
        Statistic(
            label = "Sessions",
            value = "Pie chart\n" +
                    "Pie chart\n" +
                    "Pie chart\n" +
                    "Pie chart\n" +
                    "Pie chart\n",
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(5.dp, 5.dp, 5.dp, 0.dp),
        )
        Row(
            modifier = Modifier
                .padding(0.dp, 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(

            ) {
                Statistic(
                    label = "+2 rate",
                    value = "5%",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(5.dp, 5.dp),
                )
                Statistic(
                    label = "DNF rate",
                    value = "3%",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(5.dp, 5.dp),
                )
            }
        }
    }
}
/*
global stats
TODO: total solves
TODO: time spent solving
TODO: favorite event
TODO: Pie chart of sessions and times spent/num of solves
TODO: best pb streak
TODO: % of +2
TODO: % of DNF
 */