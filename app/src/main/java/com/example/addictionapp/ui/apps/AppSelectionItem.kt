package com.example.addictionapp.ui.apps

import android.util.Log
import android.widget.CompoundButton
import com.example.addictionapp.R
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_application_selection.view.*


class AppSelectionItem(private val application: ApplicationWithIcon) : Item(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply{
            applicationIcon.setImageDrawable(application.icon)
            applicationName.text = application.name
        }

        viewHolder.itemView.applicationSelected.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                application.addToBlacklistedApps(application.name, application.packageName)
            } else {
                application.removeFromBlacklistedApps(application.name, application.packageName)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_application_selection

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount / 1
    }
}