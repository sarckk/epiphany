package com.example.addictionapp.ui.reflection.detail

import android.util.Log
import androidx.lifecycle.*
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReflectionDetailViewModel (
    private val reflectionRepository: ReflectionRepository
): ViewModel() {

    private val _dateCreated = MutableLiveData<String>()

    val reflectionItem: LiveData<Reflection>  = Transformations.switchMap(_dateCreated){
       liveData {
           emit(reflectionRepository.getReflection((it)))
       }
    }

    private val _navToListView = MutableLiveData<Event<Unit>>()
    val navToListView: LiveData<Event<Unit>> = _navToListView

    fun fetchReflection(dateCreated: String) {
        _dateCreated.value = dateCreated
    }

    fun deleteReflection(reflection: Reflection) = viewModelScope.launch {
        reflectionRepository.deleteReflection(reflection)
        _navToListView.value = Event(Unit)
    }
}