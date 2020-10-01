package com.example.addictionapp.ui.reflection.list

import com.example.addictionapp.R
import com.example.addictionapp.data.models.Reflection
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_reflection.view.*

class ReflectionListItem(private val reflection: Reflection) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply{
            reflectionCreatedDateText.text = reflection.dateCreated
        }
    }

    override fun getLayout(): Int = R.layout.item_reflection

    override fun getSpanSize(spanCount: Int, position: Int): Int {
       return spanCount / 2
    }
}