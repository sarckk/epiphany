package com.example.addictionapp.ui.apps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Application
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.ui.MainActivity
import com.example.addictionapp.ui.reflection.create.WhatElseViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_app_selection.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.item_application_selection.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class AppSelectionActivity : AppCompatActivity() {
    private val viewModel by viewModel<AppSelectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("abcd", "arrived at app selection activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_selection)
        Log.d("abcd", "loaded layout")

        fetchingAppsScreen.visibility = View.VISIBLE
        val packageManager = applicationContext.packageManager
        viewModel.startFetchAppList(packageManager)
        bindUIToViewModel()
    }

    private fun bindUIToViewModel() {
        viewModel.appList.observe(this, Observer {
            Log.d("abcd", "Getting ${it}")
            if(it != null && it.isNotEmpty()){
                updateRecycler(it)
            }
        })

        viewModel.loaded.observe(this, Observer {
            if(it){
                fetchingAppsScreen.visibility = View.GONE
            }
        })


        blacklistAppsButton.setOnClickListener {
            val applications = viewModel.blacklistedApplications
            applications.forEach {
                viewModel.upsertApplication(it)
            }

            val gotoMain = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(gotoMain)
            finish()
        }
    }

    private fun updateRecycler(applications: List<ApplicationWithIcon>) {
        val applicationListItems = applications.toApplicationItem()

        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 1
            addAll(applicationListItems)
        }

        applicationRecyclerView.apply {
            layoutManager = GridLayoutManager(context, groupieAdapter.spanCount).apply{
                spanSizeLookup = groupieAdapter.spanSizeLookup
            }
            adapter = groupieAdapter
        }
    }

    private fun List<ApplicationWithIcon>.toApplicationItem() : List<AppSelectionItem> {
        return this.map { application ->
            AppSelectionItem(application)
        }
    }
}