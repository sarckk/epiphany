package com.example.addictionapp.utils

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.Intent.*
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Accessibility : AccessibilityService() {

    var isFacebookRunning : Boolean = false;
    var facebookPkgName = "com.facebook.katana";

    var mAutoService : Accessibility? = null;

    override fun onInterrupt() {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) { /* Do something */
        launchSplitScreen()
    }

    override fun onServiceConnected() {
        super.onServiceConnected();
        mAutoService = this
    }

    override fun onCreate() {
        EventBus.getDefault().register(this);
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}


    public fun launchSplitScreen() {
        Log.i("test", "Launching split screen...")

        var startCamera = Intent("com.example.addictionapp")
        startCamera.addFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT)
        startCamera.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startCamera.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK)

        startActivity(startCamera)

        mAutoService?.performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)
    }
}