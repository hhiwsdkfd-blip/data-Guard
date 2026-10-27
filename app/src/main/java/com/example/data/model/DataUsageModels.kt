package com.example.data.model

data class DataUsageSummary(
    val totalBytes: Long = 0L,
    val mobileBytes: Long = 0L,
    val wifiBytes: Long = 0L,
    val rxBytes: Long = 0L,
    val txBytes: Long = 0L,
    val planLimitBytes: Long = 20L * 1024L * 1024L * 1024L, // 20 GB default limit
    val todayBytes: Long = 0L,
    val weekBytes: Long = 0L,
    val monthBytes: Long = 0L
)

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val mobileBytes: Long,
    val wifiBytes: Long,
    val totalBytes: Long,
    val rxBytes: Long = 0L,
    val txBytes: Long = 0L,
    val percentage: Float = 0f,
    val brandColorHex: Long = 0xFF00D2C4
)

data class HourlyUsagePoint(
    val hourLabel: String,
    val mobileBytes: Long,
    val wifiBytes: Long
)

data class DailyUsagePoint(
    val dateLabel: String,
    val totalBytes: Long,
    val mobileBytes: Long,
    val wifiBytes: Long
)

data class DateRangeSelection(
    val startMillis: Long,
    val endMillis: Long,
    val displayText: String,
    val period: TimePeriod = TimePeriod.TODAY
)
