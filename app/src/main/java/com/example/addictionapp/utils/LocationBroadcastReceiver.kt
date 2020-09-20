package com.example.addictionapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.addictionapp.api.BackendCalls
import com.huawei.hms.location.ActivityIdentificationData
import com.huawei.hms.location.ActivityIdentificationResponse


class LocationBroadcastReceiver : BroadcastReceiver() {
    val backend = BackendCalls()

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_LOCATION == action) {
                val activityIdentificationResponse =
                    ActivityIdentificationResponse.getDataFromIntent(intent)
                val list =
                    activityIdentificationResponse.activityIdentificationDatas

                var result = ""
                when(list[0].identificationActivity) {
                    ActivityIdentificationData.VEHICLE -> result = "VEHICLE"
                    ActivityIdentificationData.BIKE -> result = "BIKE"
                    ActivityIdentificationData.FOOT -> result = "FOOT"
                    ActivityIdentificationData.STILL -> result = "STILL"
                    ActivityIdentificationData.OTHERS -> result = "OTHERS"
                    ActivityIdentificationData.TILTING -> result = "TILTING"
                    ActivityIdentificationData.WALKING -> result = "WALKING"
                    ActivityIdentificationData.RUNNING -> result = "RUNNING"
                }

                context.getSharedPreferences("test", Context.MODE_PRIVATE).getString("key", "")?.let {
                    backend.sendAccelerometer(it, result)
                }
            }
        }
    }

    companion object {
        const val ACTION_PROCESS_LOCATION =
            "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
    }
}