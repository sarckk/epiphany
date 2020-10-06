package com.example.addictionapp.ui.splash

import androidx.lifecycle.*
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.models.Application
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SplashViewModel(
    val blocklistRepository: BlocklistRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val blocklistedAppsList : List<Application> =
        blocklistRepository.getAllBlacklistedApps()
}