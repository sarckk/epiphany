package com.example.addictionapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.addictionapp.R
import com.example.addictionapp.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity: AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.onboarding_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}