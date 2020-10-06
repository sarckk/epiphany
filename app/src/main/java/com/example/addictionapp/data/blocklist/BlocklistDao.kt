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

    @Delete
    suspend fun delete(application: Application)
}