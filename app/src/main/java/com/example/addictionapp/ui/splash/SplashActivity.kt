package com.example.addictionapp.ui.splash

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.addictionapp.ui.MainActivity
import com.example.addictionapp.ui.apps.AppSelectionActivity
import com.example.addictionapp.ui.permissions.PermissionsActivity
import com.example.addictionapp.ui.reflection.list.ReflectionListViewModel
import kotlinx.android.synthetic.main.fragment_reflection_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity: AppCompatActivity() {
    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usagePermissionGranted = getUsagePermissionStatus()
        val hasBlacklistedApps = hasBlacklistApps()
        Log.d("TEST", usagePermissionGranted.toString())

        if(usagePermissionGranted) {
            if (hasBlacklistedApps) {
                redirectToMain()
            } else {
                redirectToBlacklist()
            }
        }else {
            redirectToPermissions()
        }

    }

    private fun redirectToPermissions() {
        val gotoPermissions = Intent(this, PermissionsActivity::class.java)
        gotoPermissions.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        Log.d("TEST", "REDIRECT TO PERMISSIONS")
        startActivity(gotoPermissions)
        finish()
    }

    private fun redirectToBlacklist() {
        val gotoBlacklist = Intent(this, AppSelectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        Log.d("TEST", "REDIRECT TO BLACKLIST")
        startActivity(gotoBlacklist)
        finish()
    }

    private fun redirectToMain() {
        val gotoMain = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        Log.d("TEST", "REDIRECT TO MAIN")
        startActivity(gotoMain)
        finish()
    }

    private fun getUsagePermissionStatus(): Boolean {
        val opsService= getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return opsService.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName) == AppOpsManager.MODE_ALLOWED
    }

    private fun hasBlacklistApps(): Boolean {
        return viewModel.blocklistedAppsList.isNotEmpty()
    }
}
