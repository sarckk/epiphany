package com.example.addictionapp.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.addictionapp.R
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        overviewToolbarText.setText(R.string.app_name)

        //setUpSparkview()

        // TODO: Remove before production
        test.setOnClickListener {
            findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToReflectionListFragment(null))
        }
    }


}