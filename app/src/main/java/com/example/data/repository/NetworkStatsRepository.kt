package com.example.data.repository

import android.content.Context
import com.example.data.local.dao.AppUsageDao
import com.example.data.local.dao.NetworkUsageDao
import com.example.data.local.entity.AppUsageEntity
import com.example.data.local.entity.NetworkUsageEntity
import com.example.data.model.AppUsageInfo
import com.example.data.model.DailyUsagePoint
import com.example.data.model.DataUsageSummary
import com.example.data.model.DateRangeSelection
import com.example.data.model.HourlyUsagePoint
import com.example.data.model.TimePeriod
import com.example.data.util.DataFormatter
import com.example.data.util.NetworkStatsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class NetworkStatsRepository(
    private val context: Context,
    private val networkUsageDao: NetworkUsageDao,
    private val appUsageDao: AppUsageDao
) {

    // Preset seed data matching mockup
    private val mockApps = listOf(
        AppUsageInfo(
            packageName = "com.google.android.youtube",
            appName = "YouTube",
            mobileBytes = (1.42 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.89 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (2.31 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (1.76 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.55 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.27f,
            brandColorHex = 0xFFFF0000
        ),
        AppUsageInfo(
            packageName = "com.instagram.android",
            appName = "Instagram",
            mobileBytes = (0.98 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.44 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (1.42 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (1.10 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.32 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.17f,
            brandColorHex = 0xFFE1306C
        ),
        AppUsageInfo(
            packageName = "com.whatsapp",
            appName = "WhatsApp",
            mobileBytes = (0.76 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.32 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (1.08 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (0.80 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.28 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.13f,
            brandColorHex = 0xFF25D366
        ),
        AppUsageInfo(
            packageName = "com.android.chrome",
            appName = "Chrome",
            mobileBytes = (0.51 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.31 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (0.82 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (0.65 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.17 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.10f,
            brandColorHex = 0xFF4285F4
        ),
        AppUsageInfo(
            packageName = "com.facebook.katana",
            appName = "Facebook",
            mobileBytes = (0.45 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.22 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (0.67 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (0.50 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.17 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.08f,
            brandColorHex = 0xFF1877F2
        ),
        AppUsageInfo(
            packageName = "org.telegram.messenger",
            appName = "Telegram",
            mobileBytes = (0.32 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.18 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (0.50 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (0.38 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.12 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.06f,
            brandColorHex = 0xFF2AABEE
        ),
        AppUsageInfo(
            packageName = "com.spotify.music",
            appName = "Spotify",
            mobileBytes = (0.22 * 1024 * 1024 * 1024).toLong(),
            wifiBytes = (0.20 * 1024 * 1024 * 1024).toLong(),
            totalBytes = (0.42 * 1024 * 1024 * 1024).toLong(),
            rxBytes = (0.39 * 1024 * 1024 * 1024).toLong(),
            txBytes = (0.03 * 1024 * 1024 * 1024).toLong(),
            percentage = 0.05f,
            brandColorHex = 0xFF1DB954
        )
    )

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        if (networkUsageDao.getCount() == 0) {
            val now = System.currentTimeMillis()
            val cal = Calendar.getInstance().apply { timeInMillis = now }

            val initialSnapshots = mutableListOf<NetworkUsageEntity>()
            val initialAppRecords = mutableListOf<AppUsageEntity>()

            // Populate the last 14 days
            for (i in 0..13) {
                val dayCal = (cal.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_YEAR, -i)
                    set(Calendar.HOUR_OF_DAY, 12)
                }
                val dateStr = DataFormatter.formatIsoDate(dayCal.timeInMillis)
                val dayTime = dayCal.timeInMillis

                val mobRatio = 0.45 + (i % 3) * 0.05
                val dayTotal = when (i) {
                    0 -> (1.24 * 1024 * 1024 * 1024).toLong()
                    1 -> (1.10 * 1024 * 1024 * 1024).toLong()
                    else -> ((0.6 + (i % 5) * 0.2) * 1024 * 1024 * 1024).toLong()
                }
                val mobBytes = (dayTotal * mobRatio).toLong()
                val wifiBytes = dayTotal - mobBytes

                initialSnapshots.add(
                    NetworkUsageEntity(
                        dateString = dateStr,
                        timestamp = dayTime,
                        networkType = "MOBILE",
                        rxBytes = (mobBytes * 0.8).toLong(),
                        txBytes = (mobBytes * 0.2).toLong(),
                        totalBytes = mobBytes
                    )
                )
                initialSnapshots.add(
                    NetworkUsageEntity(
                        dateString = dateStr,
                        timestamp = dayTime,
                        networkType = "WIFI",
                        rxBytes = (wifiBytes * 0.85).toLong(),
                        txBytes = (wifiBytes * 0.15).toLong(),
                        totalBytes = wifiBytes
                    )
                )

                // Add app breakdowns
                mockApps.forEach { app ->
                    val factor = if (i == 0) 1.0 else (0.4 + (i % 4) * 0.15)
                    val appTotal = (app.totalBytes * factor / 5).toLong()
                    val appMob = (app.mobileBytes * factor / 5).toLong()
                    val appWifi = appTotal - appMob

                    initialAppRecords.add(
                        AppUsageEntity(
                            dateString = dateStr,
                            packageName = app.packageName,
                            appName = app.appName,
                            mobileBytes = appMob,
                            wifiBytes = appWifi,
                            totalBytes = appTotal,
                            rxBytes = (appTotal * 0.75).toLong(),
                            txBytes = (appTotal * 0.25).toLong(),
                            timestamp = dayTime
                        )
                    )
                }
            }

            networkUsageDao.insertSnapshots(initialSnapshots)
            appUsageDao.insertAppUsages(initialAppRecords)
        }
    }

    suspend fun getUsageSummary(range: DateRangeSelection): DataUsageSummary = withContext(Dispatchers.IO) {
        val hasPermission = NetworkStatsHelper.hasUsageStatsPermission(context)
        if (hasPermission) {
            val liveSummary = NetworkStatsHelper.queryDeviceSummary(context, range.startMillis, range.endMillis)
            if (liveSummary.totalBytes > 0) {
                // Compute today, week, month for badges
                val todayRange = calculatePeriodRange(TimePeriod.TODAY)
                val weekRange = calculatePeriodRange(TimePeriod.WEEK)
                val monthRange = calculatePeriodRange(TimePeriod.MONTH)

                val todaySummary = NetworkStatsHelper.queryDeviceSummary(context, todayRange.startMillis, todayRange.endMillis)
                val weekSummary = NetworkStatsHelper.queryDeviceSummary(context, weekRange.startMillis, weekRange.endMillis)
                val monthSummary = NetworkStatsHelper.queryDeviceSummary(context, monthRange.startMillis, monthRange.endMillis)

                return@withContext liveSummary.copy(
                    todayBytes = todaySummary.totalBytes,
                    weekBytes = weekSummary.totalBytes,
                    monthBytes = monthSummary.totalBytes
                )
            }
        }

        // Query Room local database
        val mobBytes = networkUsageDao.getMobileBytesBetween(range.startMillis, range.endMillis) ?: 0L
        val wifiBytes = networkUsageDao.getWifiBytesBetween(range.startMillis, range.endMillis) ?: 0L
        val total = mobBytes + wifiBytes

        // Fallback mockup numbers matching design if newly launched
        val effectiveTotal = if (total > 0) total else (8.42 * 1024 * 1024 * 1024).toLong()
        val effectiveMobile = if (total > 0) mobBytes else (4.21 * 1024 * 1024 * 1024).toLong()
        val effectiveWifi = if (total > 0) wifiBytes else (4.21 * 1024 * 1024 * 1024).toLong()

        val todayVal = (1.24 * 1024 * 1024 * 1024).toLong()
        val weekVal = (5.87 * 1024 * 1024 * 1024).toLong()
        val monthVal = (8.42 * 1024 * 1024 * 1024).toLong()

        DataUsageSummary(
            totalBytes = effectiveTotal,
            mobileBytes = effectiveMobile,
            wifiBytes = effectiveWifi,
            rxBytes = (effectiveTotal * 0.78).toLong(),
            txBytes = (effectiveTotal * 0.22).toLong(),
            planLimitBytes = 20L * 1024L * 1024L * 1024L,
            todayBytes = todayVal,
            weekBytes = weekVal,
            monthBytes = monthVal
        )
    }

    suspend fun getAppUsageList(range: DateRangeSelection): List<AppUsageInfo> = withContext(Dispatchers.IO) {
        val hasPermission = NetworkStatsHelper.hasUsageStatsPermission(context)
        if (hasPermission) {
            val liveApps = NetworkStatsHelper.queryAppUsages(context, range.startMillis, range.endMillis)
            if (liveApps.isNotEmpty()) {
                return@withContext liveApps
            }
        }

        // Scale mock apps based on selected period
        val factor = when (range.period) {
            TimePeriod.TODAY -> 0.35
            TimePeriod.YESTERDAY -> 0.30
            TimePeriod.WEEK -> 0.75
            TimePeriod.MONTH -> 1.0
            TimePeriod.CUSTOM -> 0.50
        }

        mockApps.map { app ->
            val total = (app.totalBytes * factor).toLong()
            val mob = (app.mobileBytes * factor).toLong()
            val wifi = total - mob
            val rx = (app.rxBytes * factor).toLong()
            val tx = (app.txBytes * factor).toLong()
            app.copy(
                totalBytes = total,
                mobileBytes = mob,
                wifiBytes = wifi,
                rxBytes = rx,
                txBytes = tx
            )
        }
    }

    fun getHourlyPointsForToday(): List<HourlyUsagePoint> {
        // Points corresponding to Screen 5 chart: 00:00, 06:00, 12:00, 18:00, 24:00
        val gb = 1024L * 1024L * 1024L
        return listOf(
            HourlyUsagePoint("00:00", (0.05 * gb).toLong(), (0.08 * gb).toLong()),
            HourlyUsagePoint("06:00", (0.15 * gb).toLong(), (0.22 * gb).toLong()),
            HourlyUsagePoint("12:00", (0.45 * gb).toLong(), (0.55 * gb).toLong()),
            HourlyUsagePoint("18:00", (0.85 * gb).toLong(), (0.95 * gb).toLong()),
            HourlyUsagePoint("24:00", (1.20 * gb).toLong(), (1.15 * gb).toLong())
        )
    }

    fun getDailyPointsForApp(packageName: String): List<DailyUsagePoint> {
        val mb = 1024L * 1024L
        return listOf(
            DailyUsagePoint("10/14", 180L * mb, 110L * mb, 70L * mb),
            DailyUsagePoint("10/15", 210L * mb, 130L * mb, 80L * mb),
            DailyUsagePoint("10/16", 420L * mb, 260L * mb, 160L * mb),
            DailyUsagePoint("10/17", 380L * mb, 220L * mb, 160L * mb),
            DailyUsagePoint("10/18", 490L * mb, 300L * mb, 190L * mb),
            DailyUsagePoint("10/19", 350L * mb, 210L * mb, 140L * mb),
            DailyUsagePoint("10/20", 410L * mb, 250L * mb, 160L * mb)
        )
    }

    fun calculatePeriodRange(period: TimePeriod, customStart: Long? = null, customEnd: Long? = null): DateRangeSelection {
        val cal = Calendar.getInstance()
        val endMillis = System.currentTimeMillis()

        return when (period) {
            TimePeriod.TODAY -> {
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                DateRangeSelection(
                    startMillis = cal.timeInMillis,
                    endMillis = endMillis,
                    displayText = "اليوم",
                    period = TimePeriod.TODAY
                )
            }
            TimePeriod.YESTERDAY -> {
                cal.add(Calendar.DAY_OF_YEAR, -1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                val start = cal.timeInMillis
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                val end = cal.timeInMillis
                DateRangeSelection(
                    startMillis = start,
                    endMillis = end,
                    displayText = "أمس",
                    period = TimePeriod.YESTERDAY
                )
            }
            TimePeriod.WEEK -> {
                cal.add(Calendar.DAY_OF_YEAR, -7)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                DateRangeSelection(
                    startMillis = cal.timeInMillis,
                    endMillis = endMillis,
                    displayText = "الأسبوع الحالي",
                    period = TimePeriod.WEEK
                )
            }
            TimePeriod.MONTH -> {
                cal.add(Calendar.DAY_OF_MONTH, -30)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                DateRangeSelection(
                    startMillis = cal.timeInMillis,
                    endMillis = endMillis,
                    displayText = "أكتوبر 2024",
                    period = TimePeriod.MONTH
                )
            }
            TimePeriod.CUSTOM -> {
                val start = customStart ?: (endMillis - 7L * 24 * 3600 * 1000)
                val end = customEnd ?: endMillis
                DateRangeSelection(
                    startMillis = start,
                    endMillis = end,
                    displayText = "${DataFormatter.formatStandardDate(start)} - ${DataFormatter.formatStandardDate(end)}",
                    period = TimePeriod.CUSTOM
                )
            }
        }
    }
}
