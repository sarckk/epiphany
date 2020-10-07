package com.example.addictionapp.ui

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.addictionapp.R
import com.example.addictionapp.services.AppTrackingService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()
        setUpBottomNav()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        AppTrackingService.startService(this)
    }

    override fun onDestroy() {
        AppTrackingService.stopService(this)
        super.onDestroy()
    }

    private fun setUpToolbar() {
        main_toolbar_text.setText(R.string.app_name)
        main_toolbar.inflateMenu(R.menu.menu_overview)
    }

    private fun setUpBottomNav(){
        main_bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottom_nav_statistics -> {
                    navController.navigate(R.id.overviewFragment)
                }
                R.id.bottom_nav_reflections -> {
                    navController.navigate(R.id.reflectionListFragment)
                }
                R.id.bottom_nav_activities -> {
                    navController.navigate(R.id.reflectionListFragment)
                }
            }
            true
        }
    }
}
