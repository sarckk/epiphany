package com.example.addictionapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.addictionapp.data.models.Reflection

@Dao
interface ReflectionDao{
    @Query("SELECT * FROM reflection_table ORDER BY date_created")
    suspend fun getAllReflections(): List<Reflection>

    @Query("SELECT * FROM reflection_table WHERE date_created = :creationDate")
    suspend fun getReflection(creationDate: String): Reflection

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reflection: Reflection)

    @Delete
    suspend fun delete(reflection: Reflection)
}