package com.example.addictionapp.ui.suggestions

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.data.models.Suggestion
import com.example.addictionapp.ui.MainActivity
import com.example.addictionapp.ui.reflection.list.ReflectionListFragmentArgs
import com.example.addictionapp.ui.reflection.list.ReflectionListFragmentDirections
import com.example.addictionapp.ui.reflection.list.ReflectionListItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_reflection_list.*
import kotlinx.android.synthetic.main.fragment_suggestions.*
import org.koin.android.viewmodel.ext.android.viewModel

class SuggestionsFragment : Fragment() {
    private val args: SuggestionsFragmentArgs by navArgs()
    private val viewModel by viewModel<SuggestionsViewModel>()
    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>

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

        setUpRecyclerViewHelper()
        bindButtons()
        bindUIToViewModel()
    }

    private fun bindButtons(){
        finishSuggestionCreationBtn.setOnClickListener {
            if (!args.fromOnboarding) {
                // if from main screen
                findNavController().navigate(R.id.suggestionsFragment)
            } else {
                // if from the Onboarding process
                val gotoMain = Intent(requireContext(), MainActivity::class.java)
                gotoMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(gotoMain)
            }
        }

        confirmSuggestionFab.setOnClickListener {
            val text = suggestionInput.editText?.text.toString().trim()
            if (text != null && text.isNotEmpty()) {
                viewModel.addSuggestion(text)
            }
            // clear text
            suggestionInput.editText?.text?.clear()
        }
    }

    private fun setUpRecyclerViewHelper(){
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // delete item and notify data changed
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }




        ItemTouchHelper(swipeCallback).attachToRecyclerView(suggestionRecyclerView)
    }

    private fun bindUIToViewModel() {
        viewModel.newSuggestions.observe(viewLifecycleOwner, Observer { suggestions ->
            Log.d("TEST3", "${suggestions}")
            if (suggestions.isNotEmpty()) {
                noSuggestionImg.visibility = View.GONE
                noSuggestionText.visibility = View.GONE
                updateRecycler(suggestions.reversed())
            } else {
                noSuggestionImg.visibility = View.VISIBLE
                noSuggestionText.visibility = View.VISIBLE
                suggestionRecyclerView.visibility = View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (!it) {
                suggestionsLoadingScreen.visibility = View.GONE
            }
        })
    }

    private fun updateRecycler(suggestions: List<Suggestion>) {
        val suggestionItems = suggestions.toSuggestionItem()
        groupAdapter =  GroupAdapter<GroupieViewHolder>().apply{
            setOnItemClickListener { item, view ->
                val position = getAdapterPosition(item)
            }
            addAll(suggestionItems)
        }
        suggestionRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    private fun List<Suggestion>.toSuggestionItem(): List<SuggestionItem> {
        return this.map {
            SuggestionItem(it)
        }
    }
}