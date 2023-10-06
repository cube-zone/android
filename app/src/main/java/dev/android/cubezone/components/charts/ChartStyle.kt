package dev.android.cubezone.components.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color

class ChartStyle(
    val bgColor: Color = Color.Transparent,
    val lineColor: Color = Color.Red,
    val lineThickness: Float = 2f,
    val lineType: Int = LineType.Straight,
    val axisLineColor: Color = Color.White,
    val axisLabelColor: Color = Color.White,
    val gridLinesColor: Color = Color.White,

    val showVerticalGridLines: Boolean = false,
    val showHorizontalGridLines: Boolean = false,
    val showAxes: Boolean = true,
)

class LineType {
    companion object {
        const val Straight = 0
        const val Curved = 1
    }
}