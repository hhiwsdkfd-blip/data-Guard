package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AppUsageDao
import com.example.data.local.dao.NetworkUsageDao
import com.example.data.local.entity.AppUsageEntity
import com.example.data.local.entity.NetworkUsageEntity

@Database(
    entities = [
        NetworkUsageEntity::class,
        AppUsageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataGuardDatabase : RoomDatabase() {

    abstract fun networkUsageDao(): NetworkUsageDao
    abstract fun appUsageDao(): AppUsageDao

    companion object {
        @Volatile
        private var INSTANCE: DataGuardDatabase? = null

        fun getInstance(context: Context): DataGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataGuardDatabase::class.java,
                    "dataguard_pro.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
