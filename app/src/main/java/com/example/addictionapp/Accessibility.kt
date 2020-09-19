package com.example.addictionapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.Intent.*
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class Accessibility : AccessibilityService() {

    var isFacebookRunning : Boolean = false;
    var facebookPkgName = "com.facebook.katana";

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected();
        mAutoService = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    companion object {
        var mAutoService : Accessibility? = null;
        public fun getInstance(): Accessibility? {
            return mAutoService;
        }
    }

    public fun launchSplitScreen() {
        Log.i("test", "Launching split screen...")

        /*var startCamera = Intent("com.example.addictionapp")
        startCamera.addFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT)
        startCamera.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startCamera.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK)

        startActivity(startCamera)*/

        mAutoService?.performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)
    }
}