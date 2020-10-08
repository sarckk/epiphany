package com.example.addictionapp.data.blocklist

import com.example.addictionapp.data.models.Application
import kotlinx.coroutines.flow.Flow

interface BlocklistRepository {
    fun getAllBlacklistedApps(): List<Application>
    suspend fun upsertApplication(application: Application)
    suspend fun deleteApplication(packageName: String)
}