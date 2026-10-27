package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.DailyUsagePoint
import com.example.data.util.DataFormatter
import com.example.ui.components.AppIconBadge
import com.example.ui.components.WeeklyBarChart
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MobileGreen
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import com.example.ui.theme.WifiCyan

@Composable
fun AppDetailScreen(
    app: AppUsageInfo,
    dailyPoints: List<DailyUsagePoint>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("app_detail_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "رجوع",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "تفاصيل التطبيق",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Header Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconBadge(
                appName = app.appName,
                brandColor = Color(app.brandColorHex),
                size = 52
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextGrayMuted,
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Summary 3-Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailStatBox(
                label = "إجمالي الاستهلاك",
                value = DataFormatter.formatBytes(app.totalBytes),
                modifier = Modifier.weight(1f)
            )
            DetailStatBox(
                label = "بيانات الجوال",
                value = DataFormatter.formatBytes(app.mobileBytes),
                modifier = Modifier.weight(1f)
            )
            DetailStatBox(
                label = "Wi-Fi",
                value = DataFormatter.formatBytes(app.wifiBytes),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Download and Upload Section
        Text(
            text = "التنزيل والرفع",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Upload Box
            Box(
                modifier = Modifier
                    .weight(1f)
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
                        Text(
                            text = "رفع",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayLight,
                                fontSize = 13.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = WifiCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = DataFormatter.formatBytes(app.txBytes),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    )
                }
            }

            // Download Box
            Box(
                modifier = Modifier
                    .weight(1f)
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
                        Text(
                            text = "تنزيل",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayLight,
                                fontSize = 13.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = MobileGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = DataFormatter.formatBytes(app.rxBytes),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Consumption Over Days Chart
        WeeklyBarChart(days = dailyPoints)

        Spacer(modifier = Modifier.height(20.dp))

        // Time Period Range Section
        Text(
            text = "الفترة الزمنية",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TimePeriodBox(label = "من", dateText = "2024/10/14", modifier = Modifier.weight(1f))
            TimePeriodBox(label = "إلي", dateText = "2024/10/20", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailStatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayLight,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun TimePeriodBox(
    label: String,
    dateText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayMuted,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            )
        }
    }
}
