package com.example.addictionapp.ui.reflection.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WhatElseViewModel(
    private val reflectionRepository: ReflectionRepository
): ViewModel() {

    private val _newReflectionCreatedEvent = MutableLiveData<Unit>()
    val newReflectionCreatedEvent: LiveData<Unit> = _newReflectionCreatedEvent

    companion object {
        const val TAG = "WHAT_ELSE_VIEW_MODEL"
    }

    fun addReflection(wellbeingState: String, whatElseText: String?) = viewModelScope.launch {
        Log.d(TAG, "Adding: $wellbeingState and $whatElseText")
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val formattedDate = formatter.format(currentTime)
        val reflection = Reflection(formattedDate, wellbeingState, whatElseText)
        reflectionRepository.upsertReflection(reflection)
        _newReflectionCreatedEvent.setValue(Unit)
    }
}
