package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyUsagePoint
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.TextGrayMuted

@Composable
fun WeeklyBarChart(
    days: List<DailyUsagePoint>,
    modifier: Modifier = Modifier
) {
    val maxBytes = (days.maxOfOrNull { it.totalBytes } ?: 1L).coerceAtLeast(1L).toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("weekly_bar_chart")
    ) {
        Column {
            Text(
                text = "الاستهلاك خلال الأيام",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bars Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                days.forEach { point ->
                    val ratio = (point.totalBytes / maxBytes).coerceIn(0.12f, 1.0f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        // Bar track & fill
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .weight(1f, fill = false)
                                .height((ratio * 100).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(AccentSkyBlue)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = point.dateLabel,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextGrayMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
