package com.example.addictionapp.ui.reflection.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.addictionapp.R
import com.example.addictionapp.utils.Constants
import kotlinx.android.synthetic.main.create_reflection_toolbar.*
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

        createReflectionToolbar.title = "${getString(R.string.create_reflection_title)} (1/2)"
        createReflectionProgress.setProgress(1, true)

        bindUI()
    }

    private fun bindUI() {
        veryGoodOption.setOnClickListener{
            navigateToNextQuestion(Constants.VERY_GOOD)
        }

        prettyGoodOption.setOnClickListener{
            navigateToNextQuestion(Constants.PRETTY_GOOD)
        }

        okOption.setOnClickListener{
            navigateToNextQuestion(Constants.OK)
        }

        prettyBadOption.setOnClickListener{
            navigateToNextQuestion(Constants.PRETTY_BAD)
        }

        veryBadOption.setOnClickListener{
            navigateToNextQuestion(Constants.VERY_BAD)
        }
    }

    private fun navigateToNextQuestion(wellBeingScore: String){
        val action = WellbeingStateFragmentDirections.actionWellbeingStateFragmentToWhatElseFragment(wellBeingScore)
        findNavController().navigate(action)
    }
}