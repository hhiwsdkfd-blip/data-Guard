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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.util.DataFormatter
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import java.util.Calendar

@Composable
fun CalendarView(
    selectedDay: Int,
    onDaySelected: (Int, Int, Int) -> Unit, // day, month, year
    modifier: Modifier = Modifier
) {
    var calendarMonth by remember { mutableStateOf(Calendar.OCTOBER) }
    var calendarYear by remember { mutableStateOf(2024) }

    val daysOfWeek = listOf("السبت", "الجمعة", "الخميس", "الأربعاء", "الثلاثاء", "الإثنين", "الأحد")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("calendar_view")
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Month Header with Prev/Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (calendarMonth == Calendar.JANUARY) {
                            calendarMonth = Calendar.DECEMBER
                            calendarYear -= 1
                        } else {
                            calendarMonth -= 1
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الشهر السابق",
                        tint = TextGrayLight
                    )
                }

                Text(
                    text = "${DataFormatter.getArabicMonthName(calendarMonth)} $calendarYear",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )

                IconButton(
                    onClick = {
                        if (calendarMonth == Calendar.DECEMBER) {
                            calendarMonth = Calendar.JANUARY
                            calendarYear += 1
                        } else {
                            calendarMonth += 1
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "الشهر التالي",
                        tint = TextGrayLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Days of Week Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                daysOfWeek.forEach { dayName ->
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextGrayMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calendar Days Grid (5 rows of 7 days)
            // Month of October 2024 starts on Tuesday
            val totalDays = 31
            val startOffset = 3 // align with Arabic calendar days

            val totalCells = 35
            for (row in 0 until 5) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - startOffset + 1

                        if (dayNumber in 1..totalDays) {
                            val isSelected = (dayNumber == selectedDay)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) AccentSkyBlue else Color.Transparent)
                                    .clickable { onDaySelected(dayNumber, calendarMonth, calendarYear) }
                                    .testTag("day_$dayNumber"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$dayNumber",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        } else {
                            // Empty cell or trailing day
                            val displayNum = if (dayNumber <= 0) 30 + dayNumber else dayNumber - totalDays
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$displayNum",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextGrayMuted.copy(alpha = 0.4f),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
