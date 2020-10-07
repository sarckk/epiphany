package com.example.addictionapp.ui.splash

import androidx.lifecycle.*
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.models.Application
import com.example.addictionapp.data.models.Reflection
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SplashViewModel(
    private val blocklistRepository: BlocklistRepository
) : ViewModel() {


    val blocklistedAppsList : List<Application> =
        blocklistRepository.getAllBlacklistedApps()
}