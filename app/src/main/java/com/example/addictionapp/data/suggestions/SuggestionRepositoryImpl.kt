package com.example.addictionapp.data.suggestions

import com.example.addictionapp.data.models.Suggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class SuggestionRepositoryImpl(
    private val suggestionDao: SuggestionDao
) : SuggestionRepository {

    override fun getAllSuggestions(): Flow<List<Suggestion>> {
        return suggestionDao.getAllSuggestions()
    }

    override fun getAllSuggestionsSync(): List<Suggestion> {
        return suggestionDao.getAllSuggestionsSync()
    }

    override suspend fun getSuggestion(id: Int): Suggestion {
        return suggestionDao.getSuggestion(id)
    }

    override suspend fun upsertSuggestion(suggestion: Suggestion) {
        suggestionDao.upsert(suggestion)
    }

    override suspend fun deleteSuggestion(id: Int){
        suggestionDao.delete(id)
    }
}
