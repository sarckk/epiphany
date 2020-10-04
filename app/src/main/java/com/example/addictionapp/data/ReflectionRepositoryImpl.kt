package com.example.addictionapp.data

import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

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