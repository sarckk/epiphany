package com.example.addictionapp.ui.suggestions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.data.models.Suggestion
import com.example.addictionapp.ui.reflection.list.ReflectionListFragmentDirections
import com.example.addictionapp.ui.reflection.list.ReflectionListItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_reflection_list.*
import kotlinx.android.synthetic.main.fragment_suggestions.*
import org.koin.android.viewmodel.ext.android.viewModel

class SuggestionsFragment : Fragment(){
    private val viewModel by viewModel<SuggestionsViewModel>()

    companion object {
        private const val TAG = "SuggestionsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        finishSuggestionCreationBtn.setOnClickListener {
            findNavController().navigate(R.id.suggestionsFragment)
        }

        confirmSuggestionFab.setOnClickListener {
            val text = suggestionInput.editText?.text.toString().trim()
            if(text != null && text.isNotEmpty()){
                viewModel.addSuggestion(text)
            }
            // clear text
            suggestionInput.editText?.text?.clear()
        }

        bindUIToViewModel()
    }

    private fun bindUIToViewModel(){
        viewModel.suggestions.observe(viewLifecycleOwner, Observer { suggestions ->
            if(suggestions.isNotEmpty()){
                noSuggestionImg.visibility = View.GONE
                noSuggestionText.visibility = View.GONE
                updateRecycler(suggestions.reversed())
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
        val suggestionItems = suggestions.toSuggestionItem()
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply{
            setOnItemClickListener { item, view ->
                val position = getAdapterPosition(item)
                val suggestionClicked = suggestionItems[position]
                Log.d(TAG, suggestionClicked.toString())
            }
            addAll(suggestionItems)
        }

        suggestionRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupieAdapter
        }
    }

    private fun List<Suggestion>.toSuggestionItem() : List<SuggestionItem>{
        return this.map {
            SuggestionItem(it)
        }
    }
}