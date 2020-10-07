package com.example.addictionapp.ui.apps

import com.example.addictionapp.data.models.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.*
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.models.ApplicationWithIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppSelectionViewModel(
    private val blocklistRepository: BlocklistRepository
) : ViewModel() {

    val blacklistedApplications: MutableList<Application> = mutableListOf()

    fun addToBlacklistedApps(name: String, packageName: String) {
        blacklistedApplications.add(Application(name, packageName))
    }

    fun removeFromBlacklistedApps(name: String, packageName: String) {
        blacklistedApplications.remove(Application(name, packageName))
    }

    private val _packageManager = MutableLiveData<PackageManager>()

    fun startFetchAppList(packageManager: PackageManager) {
        _packageManager.value = packageManager
    }

    private val _loaded = MutableLiveData<Boolean>()
    val loaded: LiveData<Boolean> = _loaded

    val appList = _packageManager.switchMap {
        liveData(Dispatchers.IO){
            _loaded.postValue( false)
            val applist = it.getInstalledApplications(PackageManager.GET_META_DATA).map { app ->
                ApplicationWithIcon (it.getApplicationIcon(app),
                    it.getApplicationLabel(app) as String,
                    app.packageName,
                    { name, packageName -> addToBlacklistedApps(name, packageName) },
                    { name, packageName -> removeFromBlacklistedApps(name, packageName) }
                )
            }
            _loaded.postValue( true)
            emit(applist)
        }
    }

    fun upsertApplication(application: Application) = viewModelScope.launch {
        blocklistRepository.upsertApplication(application)
    }
}