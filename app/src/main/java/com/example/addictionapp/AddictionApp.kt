package com.example.addictionapp

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AddictionApp : Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        startKoin {
            androidContext(this@AddictionApp)
            modules(listOf(appModule))
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
       activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}