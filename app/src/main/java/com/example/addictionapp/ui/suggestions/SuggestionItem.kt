package com.example.addictionapp.ui.suggestions

import com.example.addictionapp.R
import com.example.addictionapp.data.models.Suggestion
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SuggestionItem(private val suggestion: Suggestion) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply{
            suggestionActivityName.text = suggestion.activityName
        }
    }

    override fun getLayout(): Int = R.layout.item_suggestion
}
