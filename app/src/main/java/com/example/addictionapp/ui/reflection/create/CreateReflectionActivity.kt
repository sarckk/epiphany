package com.example.addictionapp.ui.reflection.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.addictionapp.R
import kotlinx.android.synthetic.main.activity_create_reflection.*

class CreateReflectionActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reflection)
        createReflectionToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        createReflectionToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        setSupportActionBar(createReflectionToolbar)
        //supportActionBar!!.setTitle(R.string.create_reflection_title)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.create_reflection_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}