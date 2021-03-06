package com.example.addictionapp.ui.reflection.create

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.addictionapp.R
import com.example.addictionapp.ui.MainActivity
import kotlinx.android.synthetic.main.create_reflection_toolbar.*
import kotlinx.android.synthetic.main.fragment_what_else.*
import org.koin.android.viewmodel.ext.android.viewModel

class WhatElseFragment : Fragment() {
    private val args: WhatElseFragmentArgs by navArgs()
    private val viewModel by viewModel<WhatElseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_what_else, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createReflectionToolbar.title = "${getString(R.string.create_reflection_title)} (2/2)"
        createReflectionProgress.setProgress(2, true)

        bindUIToViewModel()
    }

    private fun bindUIToViewModel() {
       submitReflection.setOnClickListener{
           if(editTextIsEmpty()){
               viewModel.addReflection(args.wellBeingState, null)
           } else{
               viewModel.addReflection(args.wellBeingState, whatElseText.text.toString())
           }
       }

        viewModel.newReflectionCreatedEvent.observe(viewLifecycleOwner, Observer {
            if(it.getContentIfNotHandled() != null){
                val action = WhatElseFragmentDirections.actionWhatElseFragmentToReflectionListFragment("Reflection successfully created")
                findNavController().navigate(action)
            }
        })
    }

    private fun editTextIsEmpty(): Boolean {
        return whatElseText.text.toString().trim().isEmpty()
    }
}