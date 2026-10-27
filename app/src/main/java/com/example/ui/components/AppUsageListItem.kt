package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppUsageInfo
import com.example.data.util.DataFormatter
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun AppUsageListItem(
    app: AppUsageInfo,
    onClick: (AppUsageInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .clickable { onClick(app) }
            .padding(12.dp)
            .testTag("app_item_${app.packageName}")
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // App Icon Avatar
                AppIconBadge(
                    appName = app.appName,
                    brandColor = Color(app.brandColorHex)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // App Info & Breakdown
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${DataFormatter.formatBytes(app.mobileBytes)} جوال  |  ${DataFormatter.formatBytes(app.wifiBytes)} Wi-Fi",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextGrayMuted,
                            fontSize = 12.sp
                        )
                    )
                }

                // Total and Percentage
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = DataFormatter.formatBytes(app.totalBytes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = DataFormatter.formatPercentage(app.percentage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = PrimaryCyan,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Percentage Progress Bar
            LinearProgressIndicator(
                progress = { app.percentage.coerceIn(0.01f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = PrimaryCyan,
                trackColor = DarkSurfaceElevated,
            )
        }
    }
}

@Composable
fun AppIconBadge(
    appName: String,
    brandColor: Color,
    modifier: Modifier = Modifier,
    size: Int = 40
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(brandColor.copy(alpha = 0.2f))
            .border(1.dp, brandColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        val initial = appName.firstOrNull()?.uppercase() ?: "A"
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = brandColor,
                fontSize = (size * 0.45).sp
            )
        )
    }
}
