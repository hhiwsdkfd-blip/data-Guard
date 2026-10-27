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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextGrayLight

@Composable
fun PeriodPillRow(
    todayFormatted: String,
    weekFormatted: String,
    monthFormatted: String,
    onPeriodClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("period_pills_row"),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PeriodItem(
            label = "اليوم",
            value = todayFormatted,
            onClick = { onPeriodClick("TODAY") },
            modifier = Modifier.weight(1f)
        )
        PeriodItem(
            label = "الأسبوع",
            value = weekFormatted,
            onClick = { onPeriodClick("WEEK") },
            modifier = Modifier.weight(1f)
        )
        PeriodItem(
            label = "الشهر",
            value = monthFormatted,
            onClick = { onPeriodClick("MONTH") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PeriodItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextGrayLight,
                    fontSize = 12.sp
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
