package com.example.addictionapp.data.suggestions

import androidx.room.*
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.data.models.Suggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionDao{
    @Query("SELECT * FROM suggestion_table")
    fun getAllSuggestions(): Flow<List<Suggestion>>

    @Query("SELECT * FROM suggestion_table WHERE id = :id")
    suspend fun getSuggestion(id: Int): Suggestion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(suggestion: Suggestion)

    @Query("DELETE FROM suggestion_table WHERE id == :id")
    suspend fun delete(id: Int)
}
