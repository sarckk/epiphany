package com.example.addictionapp.data.reflections

import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.Flow

interface ReflectionRepository {
   fun getAllReflections(): Flow<List<Reflection>>
   suspend fun getReflection(dateCreated: String): Reflection
   suspend fun upsertReflection(reflection: Reflection)
   suspend fun deleteReflection(reflection: Reflection)
}
