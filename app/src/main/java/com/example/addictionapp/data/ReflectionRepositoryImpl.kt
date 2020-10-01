package com.example.addictionapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReflectionRepositoryImpl(
    private val reflectionDao: ReflectionDao
) : ReflectionRepository {

    override suspend fun getAllReflections(): List<Reflection> {
        return reflectionDao.getAllReflections()
    }

    override suspend fun getReflection(dateCreated: String): Reflection {
        return reflectionDao.getReflection(dateCreated)
    }

    override suspend fun upsertReflection(reflection: Reflection) {
        reflectionDao.upsert(reflection)
    }

    override suspend fun deleteReflection(reflection: Reflection){
        reflectionDao.delete(reflection)
    }
}