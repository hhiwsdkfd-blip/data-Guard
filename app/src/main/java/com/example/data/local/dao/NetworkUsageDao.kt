package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NetworkUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshots(snapshots: List<NetworkUsageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: NetworkUsageEntity)

    @Query("SELECT * FROM network_usage_snapshots WHERE dateString = :dateString")
    fun getSnapshotsForDate(dateString: String): Flow<List<NetworkUsageEntity>>

    @Query("SELECT * FROM network_usage_snapshots WHERE timestamp BETWEEN :startMillis AND :endMillis ORDER BY timestamp ASC")
    fun getSnapshotsBetween(startMillis: Long, endMillis: Long): Flow<List<NetworkUsageEntity>>

    @Query("SELECT SUM(totalBytes) FROM network_usage_snapshots WHERE timestamp BETWEEN :startMillis AND :endMillis")
    suspend fun getTotalBytesBetween(startMillis: Long, endMillis: Long): Long?

    @Query("SELECT SUM(totalBytes) FROM network_usage_snapshots WHERE networkType = 'MOBILE' AND timestamp BETWEEN :startMillis AND :endMillis")
    suspend fun getMobileBytesBetween(startMillis: Long, endMillis: Long): Long?

    @Query("SELECT SUM(totalBytes) FROM network_usage_snapshots WHERE networkType = 'WIFI' AND timestamp BETWEEN :startMillis AND :endMillis")
    suspend fun getWifiBytesBetween(startMillis: Long, endMillis: Long): Long?

    @Query("SELECT COUNT(*) FROM network_usage_snapshots")
    suspend fun getCount(): Int

    @Query("DELETE FROM network_usage_snapshots")
    suspend fun clearAll()
}
