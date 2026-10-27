package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppUsages(records: List<AppUsageEntity>)

    @Query("SELECT * FROM app_usage_records WHERE dateString = :dateString ORDER BY totalBytes DESC")
    fun getAppUsagesForDate(dateString: String): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage_records WHERE timestamp BETWEEN :startMillis AND :endMillis ORDER BY totalBytes DESC")
    fun getAppUsagesBetween(startMillis: Long, endMillis: Long): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage_records WHERE packageName = :packageName ORDER BY timestamp DESC")
    fun getHistoryForPackage(packageName: String): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage_records WHERE packageName = :packageName AND timestamp BETWEEN :startMillis AND :endMillis ORDER BY timestamp ASC")
    fun getHistoryForPackageBetween(packageName: String, startMillis: Long, endMillis: Long): Flow<List<AppUsageEntity>>

    @Query("SELECT COUNT(*) FROM app_usage_records")
    suspend fun getCount(): Int

    @Query("DELETE FROM app_usage_records")
    suspend fun clearAll()
}
