package com.example.addictionapp.ui.reflection.list

import androidx.lifecycle.*
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.launch

class ReflectionListViewModel(
   private val reflectionRepository: ReflectionRepository
): ViewModel() {

    /**
    val reflectionList: LiveData<List<Reflection>> = liveData {
    val reflectionList = reflectionRepository.getAllReflections()
    _finishedFetching.value = true
    emit(reflectionList)
    }
     init {
    viewmodelscope.launch {
    _reflectionlist.value = reflectionrepository.getallreflections()
    _finishedfetching.value = true
    }
    }
     **/

    private var _reflectionList = MutableLiveData<List<Reflection>>()
    val reflectionList : LiveData<List<Reflection>>
        get() = _reflectionList

    private val _finishedFetching = MutableLiveData<Boolean>(false)
    val finishedFetching: LiveData<Boolean> = _finishedFetching

    fun getReflections() = viewModelScope.launch {
        _reflectionList.value = reflectionRepository.getAllReflections()
        _finishedFetching.value = true
    }
}