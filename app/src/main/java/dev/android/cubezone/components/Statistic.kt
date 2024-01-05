package dev.android.cubezone.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

@Composable
fun Statistic(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    secondaryColor: Color = MaterialTheme.colorScheme.surface,
) {
    Surface(
        modifier = modifier,
        tonalElevation = 3.dp,
        color = Color(ColorUtils.blendARGB(secondaryColor.toArgb(), MaterialTheme.colorScheme.surface.toArgb(), 0.9f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                if (imageVector != null) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp).size(16.dp),
                        tint = color,
                    )
                }
                if (painter != null) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp).size(16.dp),
                        tint = color,
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}