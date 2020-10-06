package com.example.addictionapp.ui

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.addictionapp.R
import com.example.addictionapp.services.AppTrackingService

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        AppTrackingService.startService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
