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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.MobileGreen
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun PermissionsPrivacyScreen(
    hasUsagePermission: Boolean,
    onRequestPermission: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("permissions_privacy_screen")
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
                text = "الصلاحيات والخصوصية",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle / Intro
        Text(
            text = "الصلاحيات المطلوبة",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "يحتاج التطبيق إلى هذه الصلاحيات لعرض إحصائيات استهلاك البيانات بدقة.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextGrayLight,
                fontSize = 13.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Permission 1: استخدام البيانات
        PermissionItemCard(
            title = "استخدام البيانات",
            subtitle = "لإظهار استهلاك البيانات عبر الجوال و Wi-Fi",
            icon = Icons.Default.DataUsage,
            isGranted = hasUsagePermission,
            badgeText = if (hasUsagePermission) "ممنوح" else "منح الإذن",
            badgeColor = if (hasUsagePermission) MobileGreen else PrimaryCyan,
            onClick = {
                if (!hasUsagePermission) {
                    onRequestPermission()
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Permission 2: الوصول إلى التطبيقات
        PermissionItemCard(
            title = "الوصول إلى التطبيقات",
            subtitle = "لإظهار استهلاك التطبيقات وتصنيفها",
            icon = Icons.Default.Apps,
            isGranted = true,
            badgeText = "ممنوح",
            badgeColor = MobileGreen,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Permission 3: البيانات التاريخية
        PermissionItemCard(
            title = "البيانات التاريخية",
            subtitle = "للحصول على إحصائيات سابقة عند توفرها",
            icon = Icons.Default.History,
            isGranted = true,
            badgeText = "ممنوح",
            badgeColor = Color(0xFFE1306C),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy Guarantee Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(18.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "خصوصيتك مهمة",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "تم تخزين البيانات محلياً على جهازك ولن يتم إرسالها إلى أي خادم خارجي نهائياً.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextGrayLight,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "معلومات إضافية",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = PrimaryCyan,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PermissionItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isGranted: Boolean,
    badgeText: String,
    badgeColor: Color,
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
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
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

            // Status Badge Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeColor.copy(alpha = 0.2f))
                    .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
