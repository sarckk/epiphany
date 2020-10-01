package com.example.addictionapp.data

import androidx.lifecycle.LiveData
import com.example.addictionapp.data.models.Reflection

interface ReflectionRepository {
   suspend fun getAllReflections(): List<Reflection>
   suspend fun getReflection(dateCreated: String): Reflection
   suspend fun upsertReflection(reflection: Reflection)
   suspend fun deleteReflection(reflection: Reflection)
}
