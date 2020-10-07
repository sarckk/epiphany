package com.example.addictionapp.ui.suggestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.addictionapp.data.models.Suggestion
import com.example.addictionapp.data.suggestions.SuggestionRepository
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class SuggestionsViewModel(private val suggestionRepository: SuggestionRepository): ViewModel(){
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val suggestions : LiveData<List<Suggestion>> =
        suggestionRepository.getAllSuggestions().onStart {
            _loading.value = true
        }.onEach{
            _loading.value = false
        }.asLiveData()
}