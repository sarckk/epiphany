package com.example.addictionapp.ui.permissions

import android.Manifest
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import com.example.addictionapp.R
import com.example.addictionapp.ui.apps.AppSelectionActivity
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionsActivity : AppCompatActivity(){
    private lateinit var navController: NavController

    companion object {
        const val TAG = "PermissionActivity"
        const val PERMISSIONS_ALL = 1

        @RequiresApi(Build.VERSION_CODES.Q)
        val PERMISSIONS = arrayOf(
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        watchForUsagePermissionChange()
        enableUsageDataAccess.setOnClickListener{
            if (!hasPermissions(this)) {
                enableCustomPermissions()
            }
            redirectToSpecificPermissionSettings()
        }
    }

    private fun hasPermissions(context: Context): Boolean = PERMISSIONS.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableCustomPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_ALL);
    }

    private fun watchForUsagePermissionChange(){
        val opsService= getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        opsService.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS, packageName, AppOpsManager.OnOpChangedListener{ op, packageName ->
            val permissionGranted =  opsService.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName) == AppOpsManager.MODE_ALLOWED
            if(permissionGranted){
                val gotoSelection = Intent(this, AppSelectionActivity::class.java)
                startActivity(gotoSelection)
            }
        })
    }

    private fun redirectToSpecificPermissionSettings(){
       val gotoSettings = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        gotoSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        gotoSettings.data = Uri.fromParts("package", packageName, null)
        try{
            startActivity(gotoSettings)
        }catch(err: ActivityNotFoundException){
            redirectToGeneralPermissionSettings()
        }
    }

    private fun redirectToGeneralPermissionSettings(){
        val gotoSettings = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        gotoSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try{
            startActivity(gotoSettings)
        }catch(err: ActivityNotFoundException){
            Toast.makeText(this, "Cannot launch settings. Please enable it manually.", Toast.LENGTH_SHORT).show()
        }
    }
}