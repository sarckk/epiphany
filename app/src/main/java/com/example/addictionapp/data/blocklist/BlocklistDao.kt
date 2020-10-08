package com.example.addictionapp.data.blocklist

import androidx.room.*
import com.example.addictionapp.data.models.Application
import kotlinx.coroutines.flow.Flow

@Dao
interface BlocklistDao {
    @Query("SELECT * FROM blacklist_table")
    fun getAllBlacklistedApps(): List<Application>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(application: Application)

    @Query("DELETE FROM blacklist_table WHERE :packageName == packageName")
    suspend fun delete(packageName: String)
}