package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "network_usage_snapshots",
    indices = [Index(value = ["dateString", "networkType"])]
)
data class NetworkUsageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateString: String,
    val timestamp: Long,
    val networkType: String, // "MOBILE" or "WIFI"
    val rxBytes: Long,
    val txBytes: Long,
    val totalBytes: Long
)

@Entity(
    tableName = "app_usage_records",
    indices = [Index(value = ["dateString", "packageName"])]
)
data class AppUsageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateString: String,
    val packageName: String,
    val appName: String,
    val mobileBytes: Long,
    val wifiBytes: Long,
    val totalBytes: Long,
    val rxBytes: Long,
    val txBytes: Long,
    val timestamp: Long
)
