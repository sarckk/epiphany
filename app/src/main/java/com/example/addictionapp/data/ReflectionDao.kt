package com.example.addictionapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao{
    @Query("SELECT * FROM reflection_table ORDER BY date_created")
    fun getAllReflections(): Flow<List<Reflection>>

    @Query("SELECT * FROM reflection_table WHERE date_created = :dateCreated")
    suspend fun getReflection(dateCreated: String): Reflection

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: Reflection)

    @Delete
    suspend fun delete(reflection: Reflection)
}