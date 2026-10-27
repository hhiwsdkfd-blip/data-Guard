package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MobileGreen
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.WifiCyan

@Composable
fun DonutUsageChart(
    mobilePercent: Float = 0.52f,
    wifiPercent: Float = 0.48f,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(1f, animationSpec = tween(800))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("donut_usage_chart")
    ) {
        Column {
            Text(
                text = "مقارنة الجوال و Wi-Fi",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Donut Ring
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val strokeWidth = 14.dp.toPx()
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                        val mobSweep = (mobilePercent * 360f * animProgress.value)
                        val wifiSweep = (wifiPercent * 360f * animProgress.value)

                        // Mobile Arc (Green)
                        drawArc(
                            color = MobileGreen,
                            startAngle = -90f,
                            sweepAngle = mobSweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Wi-Fi Arc (Cyan)
                        drawArc(
                            color = WifiCyan,
                            startAngle = -90f + mobSweep,
                            sweepAngle = wifiSweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Inside Donut Text
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(mobilePercent * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MobileGreen,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                // Legend and Details
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(MobileGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "جوال  -  ${(mobilePercent * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayLight,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(WifiCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Wi-Fi  -  ${(wifiPercent * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayLight,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
