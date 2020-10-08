package com.example.addictionapp.ui.onboarding.apps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.example.addictionapp.ui.MainActivity
import com.example.addictionapp.ui.suggestions.SuggestionsFragmentArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_app_selection.*
import org.koin.android.viewmodel.ext.android.viewModel

class AppSelectionFragment : Fragment() {
    private val args: AppSelectionFragmentArgs by navArgs()
    private val viewModel by viewModel<AppSelectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("abcd", "onCreateView")
        return inflater.inflate(R.layout.fragment_app_selection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fetchingAppsScreen.visibility = View.VISIBLE
        val packageManager = requireContext().packageManager
        viewModel.startFetchAppList(packageManager)
        bindUIToViewModel()
    }

    private fun bindUIToViewModel() {
        viewModel.appList.observe(viewLifecycleOwner, Observer {
            Log.d("abcd", "Getting ${it}")
            if(it != null && it.isNotEmpty()){
                updateRecycler(it)
            }
        })

        viewModel.loaded.observe(viewLifecycleOwner, Observer {
            Log.d("abcd", "Getting ${it.toString()}")
            if(it){
                fetchingAppsScreen.visibility = View.GONE
            }
        })


        blacklistAppsButton.setOnClickListener {
            viewModel.addBlacklistApplications.forEach {
               viewModel.upsertApplication(it)
            }

            viewModel.removeBlacklistApplications.forEach{
                viewModel.deleteApplication(it)
            }

            if(args.fromMain){
                // came from MainActivity
                Toast.makeText(context, "Sucessfully updated settings", Toast.LENGTH_SHORT).show()
            }else{
                // default: came from onboarding process
                val gotoSuggestions = AppSelectionFragmentDirections.actionOverviewFragmentToSuggestionsFragment(true)
                findNavController().navigate(gotoSuggestions)
            }

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