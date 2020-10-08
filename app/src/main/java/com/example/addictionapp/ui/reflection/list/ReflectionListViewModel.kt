package com.example.addictionapp.ui.reflection.list

import androidx.lifecycle.*
import com.example.addictionapp.data.reflections.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.onStart

class ReflectionListViewModel(private val reflectionRepository: ReflectionRepository): ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val reflectionList : LiveData<List<Reflection>> =
            reflectionRepository.getAllReflections().onStart {
               _loading.value = true
            }.onEach{
                _loading.value = false
            }.asLiveData()
}