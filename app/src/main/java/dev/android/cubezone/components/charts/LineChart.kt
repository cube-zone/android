package dev.android.cubezone.components.charts

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Point>,
    style: ChartStyle,
) {
    var chartHeight by remember { mutableStateOf(0.dp) }
    var chartWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    var xScale by remember { mutableStateOf(0f) }
    var yScale by remember { mutableStateOf(0f) }

    var maxDataValue by remember {
        var max = 0f
        for (point in data) {
            if (point.y == null) continue
            if (point.y > max) max = point.y
        }
        mutableStateOf(max)
    }
    Surface(
        modifier = modifier
            .then(modifier),
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                chartHeight = with(density) { it.size.height.toDp() }
                chartWidth = with(density) { it.size.width.toDp() }
            }
        ) {
            xScale = chartWidth.toPx()
            yScale = chartHeight.toPx()
            val xStepSize = xScale / (data.size-1)
            val yStepSize = yScale / maxDataValue

            // Start drawing lines
            var prevPoint: Offset? = null
            if (style.showAxes) {
                drawLine(
                    color = style.axisLineColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, chartHeight.toPx()),
                    strokeWidth = 2f
                )
                drawLine(
                    color = style.axisLineColor,
                    start = Offset(0f, chartHeight.toPx()),
                    end = Offset(chartWidth.toPx(), chartHeight.toPx()),
                    strokeWidth = 2f
                )
            }
            for (point in data) {
                if (point.y == null) continue
                val currentPoint = Offset(
                    -(xStepSize * point.x) + chartWidth.toPx(),
                    -(yStepSize * point.y) + chartHeight.toPx()
                )
                if (prevPoint != null) {
                    drawLine(
                        color = style.lineColor,
                        start = prevPoint,
                        end = currentPoint,
                        strokeWidth = style.lineThickness
                    )
                }
                prevPoint = currentPoint
            }
        }
    }
}
