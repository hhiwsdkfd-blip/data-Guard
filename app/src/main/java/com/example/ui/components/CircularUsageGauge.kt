package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.WifiCyan

@Composable
fun CircularUsageGauge(
    usedBytes: Long,
    limitBytes: Long,
    usedFormatted: String,
    limitFormatted: String,
    modifier: Modifier = Modifier,
    size: Dp = 210.dp
) {
    val targetSweep = if (limitBytes > 0) {
        ((usedBytes.toFloat() / limitBytes.toFloat()) * 270f).coerceIn(10f, 270f)
    } else {
        135f
    }

    val animatedSweep = remember { Animatable(0f) }
    LaunchedEffect(targetSweep) {
        animatedSweep.animateTo(
            targetValue = targetSweep,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("circular_usage_gauge"),
        contentAlignment = Alignment.Center
    ) {
        val trackColor = DarkSurfaceElevated
        val strokeWidth = 14.dp

        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)

            // Background Track Arc (270 degrees, starting at 135deg)
            drawArc(
                color = trackColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Active Glowing Gradient Arc
            val gradientBrush = Brush.sweepGradient(
                colors = listOf(
                    PrimaryCyan,
                    WifiCyan,
                    Color(0xFF0284C7),
                    PrimaryCyan
                )
            )

            drawArc(
                brush = gradientBrush,
                startAngle = 135f,
                sweepAngle = animatedSweep.value,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        // Center Content Text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "إجمالي الاستهلاك",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayLight,
                    fontSize = 13.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = usedFormatted,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "من $limitFormatted",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayLight,
                    fontSize = 12.sp
                )
            )
        }
    }
}
