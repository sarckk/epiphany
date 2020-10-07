package com.example.addictionapp.ui.apps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.example.addictionapp.ui.MainActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_app_selection.*
import org.koin.android.viewmodel.ext.android.viewModel

class AppSelectionActivity : AppCompatActivity() {
    private val viewModel by viewModel<AppSelectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_selection)

        bindUIToViewModel()
    }

    private fun addAppsToBlacklist() {
    }

    private fun bindUIToViewModel() {
        val packageManager = applicationContext.packageManager
        val applications = viewModel.getAppList(packageManager)

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

        updateRecycler(applications)
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