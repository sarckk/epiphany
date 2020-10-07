package com.example.addictionapp.data.suggestions

import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.data.models.Suggestion
import kotlinx.coroutines.flow.Flow

interface SuggestionRepository {
    fun getAllSuggestions(): Flow<List<Suggestion>>
    suspend fun getSuggestion(id: Int): Suggestion
    suspend fun upsertSuggestion(suggestion: Suggestion)
    suspend fun deleteSuggestion(suggestion: Suggestion)
}
