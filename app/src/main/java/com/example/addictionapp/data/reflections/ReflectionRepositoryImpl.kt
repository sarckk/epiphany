package com.example.addictionapp.data.reflections

import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.Flow

class ReflectionRepositoryImpl(
    private val reflectionDao: ReflectionDao
) : ReflectionRepository {

    override fun getAllReflections(): Flow<List<Reflection>> {
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