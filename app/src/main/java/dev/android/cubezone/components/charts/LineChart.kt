package dev.android.cubezone.components.charts

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Point>,
    style: ChartStyle,
    lines: List<Line> = emptyList(),
    yRange: Float,
    ySteps: Int,
) {
    var chartHeight by remember { mutableStateOf(0.dp) }
    var chartWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    var xScale by remember { mutableStateOf(0f) }
    var yScale by remember { mutableStateOf(0f) }

    var maxDataValue by remember(data) {
        var max = 0f
        for (point in data) {
            if (point.y == null) continue
            if (point.y > max) max = point.y
        }
        Log.d("LineChart", "maxDataValue: $max")
        mutableStateOf(max)
    }
    Surface(
        modifier = Modifier
            .then(modifier),
    ) {
        Row {
            YAxisLabels(yRange = yRange, ySteps = ySteps)
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
                val yStepSize = yScale / maxDataValue * (maxDataValue / yRange)
                Log.d("LineChart", "help")

                lines.forEach { line ->
                    var prevPoint: Offset? = null
                    if (style.showHorizontalGridLines) {
                        for (i in 0..ySteps) {
                            drawLine(
                                color = style.gridLinesColor,
                                start = Offset(0f, (chartHeight.toPx()/ySteps)*i),
                                end = Offset(chartWidth.toPx(), (chartHeight.toPx()/ySteps)*i),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        }
                    }
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
                    for (point in line.points) {
                        if (point.y == null) continue
                        val currentPoint = Offset(
                            -(xStepSize * point.x) + chartWidth.toPx(),
                            -(yStepSize * point.y) + chartHeight.toPx()
                        )
                        if (prevPoint != null) {
                            drawLine(
                                color = line.color,
                                start = prevPoint,
                                end = currentPoint,
                                strokeWidth = line.thickness
                            )
                        }
                        prevPoint = currentPoint
                    }
                }
            }
        }
    }
}

@Composable
fun YAxisLabels(
    yRange: Float,
    ySteps: Int,
) {
    val textBounds by remember { mutableStateOf(android.graphics.Rect()) }
    var maxWidth by remember { mutableStateOf(0.dp) }

    Canvas(modifier = Modifier.fillMaxHeight().width(maxWidth)) {
        val yStepSize = size.height / ySteps
        for (i in 0 until ySteps) {
            // draw text
            drawIntoCanvas { canvas ->
                val time = (yRange / ySteps) * (ySteps - i)
                val mins = time / 60000
                val secs = time % 60000 / 1000
                val millis = time % 1000
                val text = "${if (mins > 1) "${mins.toInt()}:" else ""}${if (mins > 1 && secs < 10) "0${secs.toInt()}" else "${if(secs%1 == 0f) secs.toInt() else secs}"}" // yes, this is ugly
                val paint = android.graphics.Paint().apply {
                    color = Color.White.toArgb()
                    textAlign = android.graphics.Paint.Align.RIGHT
                    getTextBounds(text, 0, text.length, textBounds)
                    textSize = 30f
                }
                if (textBounds.width().dp > maxWidth) {
                    maxWidth = textBounds.width().dp
                }
                canvas.nativeCanvas.drawText(
                    text,
                    size.width - 10f,
                    yStepSize * i + textBounds.height() + 15f,
                    paint
                )
            }
        }
    }
}
