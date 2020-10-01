package com.example.addictionapp.ui.reflection.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.addictionapp.R

class ReflectionDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ReflectionDetailFragment()
    }

    private lateinit var viewModel: ReflectionDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reflection_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReflectionDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}