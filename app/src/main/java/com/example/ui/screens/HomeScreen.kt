package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppUsageInfo
import com.example.data.util.DataFormatter
import com.example.ui.components.AppUsageListItem
import com.example.ui.components.CircularUsageGauge
import com.example.ui.components.PeriodPillRow
import com.example.ui.components.StatCardsRow
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.viewmodel.MainUiState

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onOpenDatePicker: () -> Unit,
    onAppClick: (AppUsageInfo) -> Unit,
    onPeriodClick: (String) -> Unit,
    onNotificationsClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val summary = uiState.usageSummary
    val totalFormatted = DataFormatter.formatBytes(summary.totalBytes)
    val limitFormatted = DataFormatter.formatBytes(summary.planLimitBytes)

    val mobFormatted = DataFormatter.formatBytes(summary.mobileBytes)
    val mobPct = if (summary.totalBytes > 0) {
        "${((summary.mobileBytes.toFloat() / summary.totalBytes.toFloat()) * 100).toInt()}%"
    } else "50%"

    val wifiFormatted = DataFormatter.formatBytes(summary.wifiBytes)
    val wifiPct = if (summary.totalBytes > 0) {
        "${((summary.wifiBytes.toFloat() / summary.totalBytes.toFloat()) * 100).toInt()}%"
    } else "50%"

    val todayFormatted = DataFormatter.formatBytes(summary.todayBytes)
    val weekFormatted = DataFormatter.formatBytes(summary.weekBytes)
    val monthFormatted = DataFormatter.formatBytes(summary.monthBytes)

    val topApps = uiState.apps.take(4)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar matching mockup
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "القائمة",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Data Guard Pro",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    )
                }

                IconButton(onClick = onNotificationsClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "الإشعارات",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Date Dropdown Selector Pill
        item {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
                    .clickable { onOpenDatePicker() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("date_range_picker_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = uiState.dateRange.displayText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextGrayLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Circular Gauge Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularUsageGauge(
                    usedBytes = summary.totalBytes,
                    limitBytes = summary.planLimitBytes,
                    usedFormatted = totalFormatted,
                    limitFormatted = limitFormatted
                )
            }
        }

        // Mobile & Wi-Fi Side-by-Side Stat Cards
        item {
            StatCardsRow(
                mobileFormatted = mobFormatted,
                mobilePercent = mobPct,
                wifiFormatted = wifiFormatted,
                wifiPercent = wifiPct
            )
        }

        // Today, Week, Month Badges
        item {
            PeriodPillRow(
                todayFormatted = todayFormatted,
                weekFormatted = weekFormatted,
                monthFormatted = monthFormatted,
                onPeriodClick = onPeriodClick
            )
        }

        // Section Title: أكثر التطبيقات استهلاكاً
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "أكثر التطبيقات استهلاكاً",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    )
                )
            }
        }

        // App Consumption Items
        items(topApps) { app ->
            AppUsageListItem(
                app = app,
                onClick = { onAppClick(app) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
