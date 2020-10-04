package com.example.addictionapp.ui.permissions

import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.addictionapp.R
import com.example.addictionapp.ui.reflection.list.ReflectionListActivity
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionsActivity : AppCompatActivity(){
    companion object {
        const val TAG = "PermissionActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        watchForUsagePermissionChange()
        enableUsageDataAccess.setOnClickListener{
            redirectToSpecificPermissionSettings()
        }
    }

    private fun watchForUsagePermissionChange(){
        val opsService= getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        opsService.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS, packageName, AppOpsManager.OnOpChangedListener{ op, packageName ->
            val permissionGranted =  opsService.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName) == AppOpsManager.MODE_ALLOWED
            if(permissionGranted){
                val gotoMain = Intent(this, ReflectionListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(gotoMain)
                finish()
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