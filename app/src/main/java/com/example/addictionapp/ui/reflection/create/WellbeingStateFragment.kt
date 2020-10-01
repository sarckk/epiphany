package com.example.addictionapp.ui.reflection.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.addictionapp.R
import com.example.addictionapp.utils.Constants
import kotlinx.android.synthetic.main.activity_create_reflection.*
import kotlinx.android.synthetic.main.fragment_wellbeing_state.*

class WellbeingStateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wellbeing_state, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "${getString(R.string.create_reflection_title)} (1/2)"
        (activity as AppCompatActivity).progressBar.setProgress(1, true)

        bindUI()
    }

    private fun bindUI() {
        veryGoodOption.setOnClickListener{
            navigateToNextQuestion(Constants.VERY_GOOD, it)
        }

        prettyGoodOption.setOnClickListener{
            navigateToNextQuestion(Constants.PRETTY_GOOD, it)
        }

        okOption.setOnClickListener{
            navigateToNextQuestion(Constants.OK, it)
        }

        prettyBadOption.setOnClickListener{
            navigateToNextQuestion(Constants.PRETTY_BAD,it)
        }

        veryBadOption.setOnClickListener{
            navigateToNextQuestion(Constants.VERY_BAD, it)
        }
    }

    private fun navigateToNextQuestion(wellBeingScore: String, view: View){
        val action = WellbeingStateFragmentDirections.actionWellbeingStateFragmentToWhatElseFragment(wellBeingScore)
        view.findNavController().navigate(action)
    }
}