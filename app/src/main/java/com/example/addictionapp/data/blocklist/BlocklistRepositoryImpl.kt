package com.example.addictionapp.data.blocklist

import com.example.addictionapp.data.models.Application
import kotlinx.coroutines.flow.Flow

class BlocklistRepositoryImpl(
    private val blocklistDao: BlocklistDao
) : BlocklistRepository {

    override fun getAllBlacklistedApps(): List<Application> {
        return blocklistDao.getAllBlacklistedApps()
    }

    override suspend fun upsertApplication(application: com.example.addictionapp.data.models.Application) {
        return blocklistDao.upsert(application)
    }

    override suspend fun deleteApplication(packageName: String) {
        return blocklistDao.delete(packageName)
    }
}