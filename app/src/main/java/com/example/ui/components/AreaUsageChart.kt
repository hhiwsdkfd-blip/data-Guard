package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HourlyUsagePoint
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.MobileGreen
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import com.example.ui.theme.WifiCyan

@Composable
fun AreaUsageChart(
    points: List<HourlyUsagePoint>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("area_usage_chart")
    ) {
        Column {
            Text(
                text = "استهلاك البيانات (اليوم)",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chart Canvas with Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Y-Axis labels (RTL: on left in chart view, or standard left)
                Column(
                    modifier = Modifier.height(160.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("2.0 GB", "1.5 GB", "1.0 GB", "0.5 GB", "0 GB").forEach { label ->
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextGrayMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Wave Curve Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(start = 50.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    val width = size.width
                    val height = size.height

                    // Grid lines
                    for (i in 0..4) {
                        val y = height * (i / 4f)
                        drawLine(
                            color = DarkSurfaceElevated.copy(alpha = 0.6f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (points.isEmpty()) return@Canvas

                    // Normalized points (max 2.0 GB = 2 * 1024^3)
                    val maxVal = 2.0 * 1024 * 1024 * 1024
                    val stepX = width / (points.size - 1).coerceAtLeast(1)

                    val linePath = Path()
                    val fillPath = Path()

                    points.forEachIndexed { index, point ->
                        val total = (point.mobileBytes + point.wifiBytes).toDouble()
                        val ratio = (total / maxVal).coerceIn(0.05, 0.95).toFloat()
                        val x = index * stepX
                        val y = height - (ratio * height)

                        if (index == 0) {
                            linePath.moveTo(x, y)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, y)
                        } else {
                            val prevX = (index - 1) * stepX
                            val prevTotal = (points[index - 1].mobileBytes + points[index - 1].wifiBytes).toDouble()
                            val prevRatio = (prevTotal / maxVal).coerceIn(0.05, 0.95).toFloat()
                            val prevY = height - (prevRatio * height)

                            val cX1 = prevX + (x - prevX) / 2f
                            val cY1 = prevY
                            val cX2 = prevX + (x - prevX) / 2f
                            val cY2 = y

                            linePath.cubicTo(cX1, cY1, cX2, cY2, x, y)
                            fillPath.cubicTo(cX1, cY1, cX2, cY2, x, y)
                        }

                        if (index == points.size - 1) {
                            fillPath.lineTo(x, height)
                            fillPath.close()
                        }
                    }

                    // Fill Gradient
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                PrimaryCyan.copy(alpha = 0.45f),
                                PrimaryCyan.copy(alpha = 0.05f)
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )

                    // Line Stroke
                    drawPath(
                        path = linePath,
                        color = PrimaryCyan,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Data Point Dots
                    points.forEachIndexed { index, point ->
                        val total = (point.mobileBytes + point.wifiBytes).toDouble()
                        val ratio = (total / maxVal).coerceIn(0.05, 0.95).toFloat()
                        val x = index * stepX
                        val y = height - (ratio * height)

                        drawCircle(
                            color = DarkSurface,
                            radius = 5.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = PrimaryCyan,
                            radius = 3.5.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            // X-Axis Hour Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEach { point ->
                    Text(
                        text = point.hourLabel,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextGrayMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MobileGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "جوال",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextGrayLight,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(WifiCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Wi-Fi",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextGrayLight,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
