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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayMuted

enum class NavDestination(val labelAr: String, val icon: ImageVector) {
    HOME("الرئيسية", Icons.Default.Home),
    REPORTS("التقارير", Icons.Default.BarChart),
    APPS("التطبيقات", Icons.Default.GridView),
    SETTINGS("الإعدادات", Icons.Default.Settings)
}

@Composable
fun BottomNavBar(
    selectedDestination: NavDestination,
    onDestinationSelect: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .border(width = 1.dp, color = DarkBorder)
            .navigationBarsPadding()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavDestination.values().forEach { destination ->
                val isSelected = selectedDestination == destination
                val tint = if (isSelected) PrimaryCyan else TextGrayMuted

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onDestinationSelect(destination) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("nav_item_${destination.name.lowercase()}")
                ) {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.labelAr,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = destination.labelAr,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = tint,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
