package com.example.addictionapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AddictionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AddictionApp)
            modules(listOf(appModule))
        }
    }
}