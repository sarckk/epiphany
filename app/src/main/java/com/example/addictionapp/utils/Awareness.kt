package com.example.addictionapp.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.capture.TimeCategoriesResponse


class Awareness {

    @SuppressLint("MissingPermission")
    fun getTimeCategory(activity: Activity) {
        Awareness.getCaptureClient(activity).timeCategories
            .addOnSuccessListener { timeCategoriesResponse ->
                val categories = timeCategoriesResponse.timeCategories
                val timeInfo = categories.timeCategories
                Log.d("EpiphanyHMS", timeInfo.toString())
            }
            .addOnFailureListener { e ->
                Log.e(
                    "EpiphanyHMS",
                    "get Time Categories failed",
                    e
                )
            }
    }
}