package com.example.data.util

import com.example.data.model.AppUsageInfo
import com.example.data.model.DataUsageSummary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DataFormatter {

    fun formatBytes(bytes: Long): String {
        if (bytes <= 0) return "0.00 MB"
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        val tb = gb / 1024.0

        return when {
            tb >= 1.0 -> String.format(Locale.US, "%.2f TB", tb)
            gb >= 1.0 -> String.format(Locale.US, "%.2f GB", gb)
            mb >= 1.0 -> String.format(Locale.US, "%.2f MB", mb)
            kb >= 1.0 -> String.format(Locale.US, "%.2f KB", kb)
            else -> "$bytes B"
        }
    }

    fun formatBytesDouble(bytes: Long): Double {
        if (bytes <= 0) return 0.0
        val mb = bytes / (1024.0 * 1024.0)
        val gb = mb / 1024.0
        return if (gb >= 1.0) {
            String.format(Locale.US, "%.2f", gb).toDoubleOrNull() ?: gb
        } else {
            String.format(Locale.US, "%.2f", mb).toDoubleOrNull() ?: mb
        }
    }

    fun getUnit(bytes: Long): String {
        val gb = bytes / (1024.0 * 1024.0 * 1024.0)
        return if (gb >= 1.0) "GB" else "MB"
    }

    fun formatPercentage(ratio: Float): String {
        val pct = (ratio * 100).toInt()
        return "$pct%"
    }

    fun formatDateArabic(millis: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = getArabicMonthName(cal.get(Calendar.MONTH))
        val year = cal.get(Calendar.YEAR)
        return "$day $month $year"
    }

    fun formatMonthYearArabic(millis: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        val month = getArabicMonthName(cal.get(Calendar.MONTH))
        val year = cal.get(Calendar.YEAR)
        return "$month $year"
    }

    fun formatStandardDate(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        return sdf.format(Date(millis))
    }

    fun formatIsoDate(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date(millis))
    }

    fun getArabicMonthName(monthIndex: Int): String {
        return when (monthIndex) {
            Calendar.JANUARY -> "يناير"
            Calendar.FEBRUARY -> "فبراير"
            Calendar.MARCH -> "مارس"
            Calendar.APRIL -> "أبريل"
            Calendar.MAY -> "مايو"
            Calendar.JUNE -> "يونيو"
            Calendar.JULY -> "يوليو"
            Calendar.AUGUST -> "أغسطس"
            Calendar.SEPTEMBER -> "سبتمبر"
            Calendar.OCTOBER -> "أكتوبر"
            Calendar.NOVEMBER -> "نوفمبر"
            Calendar.DECEMBER -> "ديسمبر"
            else -> ""
        }
    }

    fun generateCsvReport(
        summary: DataUsageSummary,
        apps: List<AppUsageInfo>,
        dateRangeLabel: String
    ): String {
        val sb = StringBuilder()
        sb.append("تقرير استهلاك البيانات - Data Guard Pro\n")
        sb.append("الفترة:,$dateRangeLabel\n\n")
        sb.append("الملخص العام:\n")
        sb.append("البيان,القيمة,بالبايت\n")
        sb.append("إجمالي الاستهلاك,${formatBytes(summary.totalBytes)},${summary.totalBytes}\n")
        sb.append("بيانات الجوال,${formatBytes(summary.mobileBytes)},${summary.mobileBytes}\n")
        sb.append("بيانات Wi-Fi,${formatBytes(summary.wifiBytes)},${summary.wifiBytes}\n")
        sb.append("التنزيل,${formatBytes(summary.rxBytes)},${summary.rxBytes}\n")
        sb.append("الرفع,${formatBytes(summary.txBytes)},${summary.txBytes}\n\n")

        sb.append("تفاصيل استهلاك التطبيقات:\n")
        sb.append("اسم التطبيق,معرف الحزمة,إجمالي الاستهلاك,بيانات الجوال,Wi-Fi,تنزيل,رفع,النسبة المئوية\n")
        apps.forEach { app ->
            sb.append("\"${app.appName}\",\"${app.packageName}\",\"${formatBytes(app.totalBytes)}\",\"${formatBytes(app.mobileBytes)}\",\"${formatBytes(app.wifiBytes)}\",\"${formatBytes(app.rxBytes)}\",\"${formatBytes(app.txBytes)}\",\"${formatPercentage(app.percentage)}\"\n")
        }
        return sb.toString()
    }
}
