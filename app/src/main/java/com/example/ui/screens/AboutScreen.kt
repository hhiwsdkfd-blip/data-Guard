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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("about_screen")
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
                text = "حول التطبيق",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Logo & Branding Center
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkSurfaceElevated)
                    .border(2.dp, PrimaryCyan, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = PrimaryCyan,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Data Guard Pro",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "الإصدار 1.0.0",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = PrimaryCyan,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "تطبيق لمراقبة استهلاك بيانات الجوال والواي فاي مع إحصائيات التطبيقات وتحليل الرسوم البيانية.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextGrayLight,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Info List Items
        AboutInfoRow(label = "المطور", value = "فريق التطوير")
        Spacer(modifier = Modifier.height(10.dp))
        AboutInfoRow(label = "البريد الإلكتروني", value = "support@dataguardpro.com")
        Spacer(modifier = Modifier.height(10.dp))
        AboutInfoRow(label = "الموقع الإلكتروني", value = "www.dataguardpro.com")
        Spacer(modifier = Modifier.height(10.dp))
        AboutActionRow(label = "سياسة الخصوصية", onClick = {})
        Spacer(modifier = Modifier.height(10.dp))
        AboutActionRow(label = "شروط الإستخدام", onClick = {})
        Spacer(modifier = Modifier.height(10.dp))
        AboutActionRow(label = "تقييم التطبيق", onClick = {})

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AboutInfoRow(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayMuted,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Composable
fun AboutActionRow(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = TextGrayMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
