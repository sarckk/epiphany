package com.example.addictionapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.addictionapp.R
import com.example.addictionapp.ui.suggestions.SuggestionsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private val viewModel by viewModel<SuggestionsViewModel>()

    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}