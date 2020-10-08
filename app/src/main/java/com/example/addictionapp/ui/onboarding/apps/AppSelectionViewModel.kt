package com.example.addictionapp.ui.onboarding.apps

import com.example.addictionapp.data.models.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.*
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.data.models.ApplicationWithIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppSelectionViewModel(
    private val blocklistRepository: BlocklistRepository
) : ViewModel() {

    val addBlacklistApplications: MutableList<Application> = mutableListOf()
    val removeBlacklistApplications: MutableList<String> = mutableListOf()

    fun toBeBlackListed(name: String, packageName: String ) {
        addBlacklistApplications.add(Application(name, packageName))
    }

    fun toBeUnblackListed(packageName: String) {
        removeBlacklistApplications.add(packageName)
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
            // will block
            val dbList = blocklistRepository.getAllBlacklistedApps()
            val applist = it.getInstalledApplications(PackageManager.GET_META_DATA).map { app ->
                Log.d("TEST", app.toString())
                val isBlacklisted = dbList.any {dbRecord ->
                    dbRecord.name == (it.getApplicationLabel(app) as String)
                }
                ApplicationWithIcon (it.getApplicationIcon(app),
                    it.getApplicationLabel(app) as String,
                    app.packageName,
                    isBlacklisted, // change this dynamically
                    { name, packageName -> toBeBlackListed(name, packageName) },
                    { packageName -> toBeUnblackListed(packageName) }
                )
            }
            _loaded.postValue( true)
            emit(applist)
        }
    }

    fun upsertApplication(application: Application) = viewModelScope.launch {
        blocklistRepository.upsertApplication(application)
    }

    fun deleteApplication(packageName: String) = viewModelScope.launch {
        blocklistRepository.deleteApplication(packageName)
    }
}