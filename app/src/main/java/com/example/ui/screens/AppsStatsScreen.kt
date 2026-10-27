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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppUsageInfo
import com.example.data.model.NetworkType
import com.example.ui.components.AppUsageListItem
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun AppsStatsScreen(
    apps: List<AppUsageInfo>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedNetworkFilter: NetworkType,
    onNetworkFilterChange: (NetworkType) -> Unit,
    onAppClick: (AppUsageInfo) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        NetworkType.ALL to "الكل",
        NetworkType.MOBILE to "جوال",
        NetworkType.WIFI to "Wi-Fi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("apps_stats_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    text = "إحصائيات التطبيقات",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(start = if (onBackClick != null) 4.dp else 8.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "الإشعارات",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "بحث",
                    tint = TextGrayMuted,
                    modifier = Modifier.size(20.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "ابحث عن تطبيق...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayMuted,
                                fontSize = 14.sp
                            )
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        singleLine = true,
                        cursorBrush = SolidColor(AccentSkyBlue),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("app_search_input")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Network Type Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            filters.forEach { (type, title) ->
                val isSelected = selectedNetworkFilter == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AccentSkyBlue else DarkSurface)
                        .border(1.dp, if (isSelected) AccentSkyBlue else DarkBorder, RoundedCornerShape(20.dp))
                        .clickable { onNetworkFilterChange(type) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) Color.White else TextGrayLight,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Apps List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (apps.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد تطبيقات مطابقة",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextGrayMuted,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            } else {
                items(apps) { app ->
                    AppUsageListItem(
                        app = app,
                        onClick = onAppClick
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
