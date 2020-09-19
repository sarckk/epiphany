package com.example.addictionapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class Accessibility : AccessibilityService() {

    var mAutoService : AccessibilityService? = null;
    var isFacebookRunning : Boolean = false;

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (!isFacebookRunning && event.packageName.toString() == "com.facebook.katana") {
                launchSplitScreen()
                isFacebookRunning = event.packageName.toString() == "com.facebook.katana"
            }
        }

    }

    override fun onServiceConnected() {
        super.onServiceConnected();
        mAutoService = this
    }

    private fun launchSplitScreen() {
        mAutoService?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)

        var startSplitScreen = Intent("com.example.addictionapp")
        startSplitScreen.addFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT)
        startSplitScreen.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startSplitScreen.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK)

        startActivity(startSplitScreen)
    }
}