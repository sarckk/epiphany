package com.example.addictionapp.ui.reflection.list

import androidx.lifecycle.*
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.launch

class ReflectionListViewModel(
   private val reflectionRepository: ReflectionRepository
): ViewModel() {

    private var _reflectionList = MutableLiveData<List<Reflection>>()
    val reflectionList : LiveData<List<Reflection>>
        get() = _reflectionList

    companion object {
        const val TAG = "REFLECTION_LIST_VIEW_MODEL"
    }

    init {
        viewModelScope.launch {
            _reflectionList.setValue(reflectionRepository.getAllReflections())
        }
    }
    /**
    val reflectionList: LiveData<List<Reflection>> = liveData {
        val reflectionList = reflectionRepository.getAllReflections()
        emit(reflectionList)
    }
    **/
}