package com.example.addictionapp.data.suggestions

import com.example.addictionapp.data.models.Suggestion
import kotlinx.coroutines.flow.Flow

class SuggestionRepositoryImpl(
    private val suggestionDao: SuggestionDao
) : SuggestionRepository {

    override fun getAllSuggestions(): Flow<List<Suggestion>> {
        return suggestionDao.getAllSuggestions()
    }

    override suspend fun getSuggestion(id: Int): Suggestion {
        return suggestionDao.getSuggestion(id)
    }

    override suspend fun upsertSuggestion(suggestion: Suggestion) {
        suggestionDao.upsert(suggestion)
    }

    override suspend fun deleteSuggestion(suggestion: Suggestion){
        suggestionDao.delete(suggestion)
    }
}
