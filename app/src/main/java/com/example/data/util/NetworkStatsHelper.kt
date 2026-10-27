package com.example.data.util

import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log
import com.example.data.model.AppUsageInfo
import com.example.data.model.DataUsageSummary

object NetworkStatsHelper {

    private const val TAG = "NetworkStatsHelper"

    fun hasUsageStatsPermission(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
                ?: return false
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            Log.e(TAG, "Error checking usage stats permission", e)
            false
        }
    }

    fun openUsageAccessSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val fallbackIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to launch usage settings", ex)
            }
        }
    }

    fun queryDeviceSummary(
        context: Context,
        startTime: Long,
        endTime: Long
    ): DataUsageSummary {
        val nsm = context.getSystemService(Context.NETWORK_STATS_SERVICE) as? NetworkStatsManager
            ?: return DataUsageSummary()

        var mobileRx = 0L
        var mobileTx = 0L
        var wifiRx = 0L
        var wifiTx = 0L

        try {
            // Wi-Fi Summary
            val wifiBucket = nsm.querySummaryForDevice(
                ConnectivityManager.TYPE_WIFI,
                null,
                startTime,
                endTime
            )
            wifiRx = wifiBucket.rxBytes
            wifiTx = wifiBucket.txBytes
        } catch (e: Exception) {
            Log.w(TAG, "Cannot query Wi-Fi stats: ${e.message}")
        }

        try {
            // Mobile Summary
            val mobileBucket = nsm.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,
                startTime,
                endTime
            )
            mobileRx = mobileBucket.rxBytes
            mobileTx = mobileBucket.txBytes
        } catch (e: Exception) {
            Log.w(TAG, "Cannot query Mobile stats: ${e.message}")
        }

        val totalMobile = mobileRx + mobileTx
        val totalWifi = wifiRx + wifiTx
        val total = totalMobile + totalWifi

        return DataUsageSummary(
            totalBytes = total,
            mobileBytes = totalMobile,
            wifiBytes = totalWifi,
            rxBytes = mobileRx + wifiRx,
            txBytes = mobileTx + wifiTx
        )
    }

    fun queryAppUsages(
        context: Context,
        startTime: Long,
        endTime: Long
    ): List<AppUsageInfo> {
        val nsm = context.getSystemService(Context.NETWORK_STATS_SERVICE) as? NetworkStatsManager
            ?: return emptyList()
        val pm = context.packageManager

        val uidStats = mutableMapOf<Int, MutableAppBucket>()

        fun processStats(stats: NetworkStats, isWifi: Boolean) {
            val bucket = NetworkStats.Bucket()
            while (stats.hasNextBucket()) {
                stats.getNextBucket(bucket)
                val uid = bucket.uid
                if (uid <= 0) continue

                val current = uidStats.getOrPut(uid) { MutableAppBucket() }
                if (isWifi) {
                    current.wifiRx += bucket.rxBytes
                    current.wifiTx += bucket.txBytes
                } else {
                    current.mobileRx += bucket.rxBytes
                    current.mobileTx += bucket.txBytes
                }
            }
            stats.close()
        }

        try {
            val wifiStats = nsm.querySummary(ConnectivityManager.TYPE_WIFI, null, startTime, endTime)
            processStats(wifiStats, isWifi = true)
        } catch (e: Exception) {
            Log.w(TAG, "Error querying Wi-Fi app summary: ${e.message}")
        }

        try {
            val mobileStats = nsm.querySummary(ConnectivityManager.TYPE_MOBILE, null, startTime, endTime)
            processStats(mobileStats, isWifi = false)
        } catch (e: Exception) {
            Log.w(TAG, "Error querying Mobile app summary: ${e.message}")
        }

        val resultList = mutableListOf<AppUsageInfo>()
        var overallTotal = 0L

        uidStats.forEach { (uid, bucket) ->
            val total = bucket.total
            if (total > 0) {
                overallTotal += total
                val packages = pm.getPackagesForUid(uid)
                val packageName = packages?.firstOrNull() ?: "uid_$uid"
                val appName = try {
                    val appInfo = pm.getApplicationInfo(packageName, 0)
                    pm.getApplicationLabel(appInfo).toString()
                } catch (e: Exception) {
                    packageName
                }

                resultList.add(
                    AppUsageInfo(
                        packageName = packageName,
                        appName = appName,
                        mobileBytes = bucket.mobileTotal,
                        wifiBytes = bucket.wifiTotal,
                        totalBytes = total,
                        rxBytes = bucket.mobileRx + bucket.wifiRx,
                        txBytes = bucket.mobileTx + bucket.wifiTx
                    )
                )
            }
        }

        // Sort descending and compute percentages
        val sorted = resultList.sortedByDescending { it.totalBytes }
        return sorted.map { app ->
            val pct = if (overallTotal > 0) app.totalBytes.toFloat() / overallTotal else 0f
            app.copy(percentage = pct)
        }
    }

    private class MutableAppBucket {
        var mobileRx: Long = 0L
        var mobileTx: Long = 0L
        var wifiRx: Long = 0L
        var wifiTx: Long = 0L

        val mobileTotal get() = mobileRx + mobileTx
        val wifiTotal get() = wifiRx + wifiTx
        val total get() = mobileTotal + wifiTotal
    }
}
