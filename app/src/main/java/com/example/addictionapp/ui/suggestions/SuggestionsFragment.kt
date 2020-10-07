package com.example.addictionapp.ui.suggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Suggestion
import com.example.addictionapp.ui.reflection.list.ReflectionListFragmentDirections
import kotlinx.android.synthetic.main.fragment_suggestions.*
import org.koin.android.viewmodel.ext.android.viewModel

class SuggestionsFragment : Fragment(){
    private val viewModel by viewModel<SuggestionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createSuggestionBtn.setOnClickListener {
            // TODO: Add suggestion flow
        }

        bindUIToViewModel()
    }

    private fun bindUIToViewModel(){
        viewModel.suggestions.observe(viewLifecycleOwner, Observer { suggestions ->
            if(suggestions.isNotEmpty()){
                noSuggestionImg.visibility = View.GONE
                noSuggestionText.visibility = View.GONE
                updateRecycler(suggestions)
            } else{
                noSuggestionImg.visibility = View.VISIBLE
                noSuggestionText.visibility = View.VISIBLE
                suggestionRecyclerView.visibility = View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if(!it){
                suggestionsLoadingScreen.visibility = View.GONE
            }
        })
    }

    private fun updateRecycler(suggestions: List<Suggestion>) {
       // TODO: Implement this
    }
}