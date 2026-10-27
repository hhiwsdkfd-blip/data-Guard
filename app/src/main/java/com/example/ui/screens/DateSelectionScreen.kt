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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimePeriod
import com.example.ui.components.CalendarView
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import java.util.Calendar

@Composable
fun DateSelectionScreen(
    currentPeriod: TimePeriod,
    onBackClick: () -> Unit,
    onApplyPeriod: (TimePeriod) -> Unit,
    onApplySpecificDay: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf(currentPeriod) }
    var selectedDay by remember { mutableIntStateOf(20) }
    var selectedMonth by remember { mutableIntStateOf(Calendar.OCTOBER) }
    var selectedYear by remember { mutableIntStateOf(2024) }

    val periods = listOf(
        TimePeriod.TODAY to "اليوم",
        TimePeriod.YESTERDAY to "أمس",
        TimePeriod.WEEK to "أسبوع",
        TimePeriod.MONTH to "شهر",
        TimePeriod.CUSTOM to "مخصص"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("date_selection_screen")
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
                text = "اختيار التاريخ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Period Filter Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { (period, title) ->
                val isSelected = selectedPeriod == period
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AccentSkyBlue else DarkSurface)
                        .border(1.dp, if (isSelected) AccentSkyBlue else DarkBorder, RoundedCornerShape(20.dp))
                        .clickable { selectedPeriod = period }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) Color.White else TextGrayLight,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Calendar Grid
        CalendarView(
            selectedDay = selectedDay,
            onDaySelected = { day, month, year ->
                selectedDay = day
                selectedMonth = month
                selectedYear = year
                selectedPeriod = TimePeriod.CUSTOM
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section: الفترة الزمنية
        Text(
            text = "الفترة الزمنية",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Time Period Dropdown Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (selectedPeriod) {
                        TimePeriod.TODAY -> "اليوم"
                        TimePeriod.YESTERDAY -> "يوم أمس"
                        TimePeriod.WEEK -> "أسبوع كامل"
                        TimePeriod.MONTH -> "شهر كامل"
                        TimePeriod.CUSTOM -> "يوم واحد ($selectedDay ${if (selectedMonth == Calendar.OCTOBER) "أكتوبر" else ""})"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextGrayMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = false))
        Spacer(modifier = Modifier.height(30.dp))

        // Apply Button
        Button(
            onClick = {
                if (selectedPeriod == TimePeriod.CUSTOM) {
                    onApplySpecificDay(selectedDay, selectedMonth, selectedYear)
                } else {
                    onApplyPeriod(selectedPeriod)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("apply_date_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AccentSkyBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "تطبيق",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}
