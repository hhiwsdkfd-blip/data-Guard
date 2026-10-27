package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun StatCardsRow(
    mobileFormatted: String,
    mobilePercent: String,
    wifiFormatted: String,
    wifiPercent: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("stat_cards_row"),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "بيانات الجوال",
            value = mobileFormatted,
            subtext = mobilePercent,
            icon = Icons.Default.BarChart,
            accentColor = MobileGreen,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Wi-Fi",
            value = wifiFormatted,
            subtext = wifiPercent,
            icon = Icons.Default.Wifi,
            accentColor = WifiCyan,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextGrayLight,
                        fontSize = 13.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayLight,
                    fontSize = 12.sp
                )
            )
        }
    }
}
