package com.example.addictionapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.huawei.hms.location.ActivityIdentificationResponse


class LocationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_LOCATION == action) {
                val activityIdentificationResponse =
                    ActivityIdentificationResponse.getDataFromIntent(intent)
                val list =
                    activityIdentificationResponse.activityIdentificationDatas
            }
        }
    }

    companion object {
        const val ACTION_PROCESS_LOCATION =
            "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
    }
}