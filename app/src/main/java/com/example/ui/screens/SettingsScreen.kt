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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onOpenPermissions: () -> Unit,
    onOpenAbout: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("settings_screen")
    ) {
        // Header
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
                text = "الإعدادات",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = if (onBackClick != null) 4.dp else 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display / Dark Mode Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = null,
                        tint = AccentSkyBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "العرض",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextGrayMuted,
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "الوضع الداكن",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                        )
                    }
                }

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onToggleDarkTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                        checkedTrackColor = AccentSkyBlue,
                        uncheckedThumbColor = TextGrayMuted,
                        uncheckedTrackColor = DarkSurfaceElevated
                    ),
                    modifier = Modifier.testTag("dark_mode_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Network Settings
        SettingsActionItem(
            title = "إعدادات الشبكة",
            subtitle = "إعدادات بيانات الجوال والإنترنت",
            icon = Icons.Default.NetworkCheck,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Data & Storage
        SettingsActionItem(
            title = "البيانات والتخزين",
            subtitle = "إدارة استخدام البيانات",
            icon = Icons.Default.Storage,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Alerts & Notifications
        SettingsActionItem(
            title = "التنبيهات",
            subtitle = "إعدادات الإشعارات",
            icon = Icons.Default.Notifications,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Permissions & Privacy
        SettingsActionItem(
            title = "الصلاحيات والخصوصية",
            subtitle = "التحقق من الصلاحيات المطلوبة",
            icon = Icons.Default.Security,
            onClick = onOpenPermissions
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Language
        SettingsActionItem(
            title = "اللغة",
            subtitle = "العربية",
            icon = Icons.Default.Language,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // About App
        SettingsActionItem(
            title = "حول التطبيق",
            subtitle = "النسخة 1.0.0",
            icon = Icons.Default.Info,
            onClick = onOpenAbout
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingsActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .testTag("settings_item_${title}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AccentSkyBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextGrayMuted,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = TextGrayMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
