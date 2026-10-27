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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.HourlyUsagePoint
import com.example.data.util.DataFormatter
import com.example.ui.components.AppIconBadge
import com.example.ui.components.AreaUsageChart
import com.example.ui.components.DonutUsageChart
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun ChartsReportsScreen(
    hourlyPoints: List<HourlyUsagePoint>,
    topApps: List<AppUsageInfo>,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onExportCsv: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("يومي", "أسبوعي", "شهري", "مقارنة")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("charts_reports_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Text(
                    text = "الرسوم البيانية والتقارير",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(start = if (onBackClick != null) 4.dp else 8.dp)
                )
            }
        }

        // Tabs Row: يومي، أسبوعي، شهري، مقارنة
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tabName ->
                    val isSelected = selectedTab == tabName
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) AccentSkyBlue else DarkSurface)
                            .border(1.dp, if (isSelected) AccentSkyBlue else DarkBorder, RoundedCornerShape(20.dp))
                            .clickable { onTabSelected(tabName) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSelected) Color.White else TextGrayLight,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }

        // Area Line Graph
        item {
            AreaUsageChart(points = hourlyPoints)
        }

        // Donut Chart comparing Mobile vs Wi-Fi
        item {
            DonutUsageChart(mobilePercent = 0.52f, wifiPercent = 0.48f)
        }

        // Section: أكثر التطبيقات استهلاكاً
        item {
            Text(
                text = "أكثر التطبيقات استهلاكاً",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                )
            )
        }

        // Top Apps Progress Bars
        items(topApps.take(3)) { app ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppIconBadge(
                                appName = app.appName,
                                brandColor = Color(app.brandColorHex),
                                size = 32
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = app.appName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Text(
                            text = DataFormatter.formatPercentage(app.percentage),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryCyan,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { app.percentage.coerceIn(0.01f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryCyan,
                        trackColor = DarkSurfaceElevated
                    )
                }
            }
        }

        // Export to CSV Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onExportCsv,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("export_csv_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(PrimaryCyan)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                    tint = PrimaryCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تصدير التقرير إلى CSV",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = PrimaryCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
