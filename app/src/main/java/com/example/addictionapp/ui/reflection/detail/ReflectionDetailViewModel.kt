package com.example.addictionapp.ui.reflection.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.launch

class ReflectionDetailViewModel (
    private val reflectionRepository: ReflectionRepository
): ViewModel() {
    private val _reflectionItem = MutableLiveData<Reflection>()
    val reflectionItem: LiveData<Reflection>  = _reflectionItem

    private val _deletedSuccessfully = MutableLiveData<Boolean>()
    val deletedSuccessfully: LiveData<Boolean> = _deletedSuccessfully

    fun fetchReflection(dateCreated: String) = viewModelScope.launch {
        reflectionRepository.getReflection(dateCreated).let {
            _reflectionItem.value = it
        }
    }

    fun deleteReflection(reflection: Reflection) = viewModelScope.launch {
        reflectionRepository.deleteReflection(reflection).let{
            _deletedSuccessfully.value = true
            _reflectionItem.value = null
        }
    }
}