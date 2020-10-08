package com.example.addictionapp.ui.onboarding.apps

import com.example.addictionapp.R
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_application_selection.view.*


private const val CHAR_LIMIT = 22

class AppSelectionItem(private val app: ApplicationWithIcon) : Item(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val wasInitiallyBlacklisted = app.isBlacklisted

        viewHolder.itemView.apply{
            applicationIcon.setImageDrawable(app.icon)
            applicationName.text = if(app.name.length > CHAR_LIMIT)
                app.name.substring(0, Math.min(app.name.length, CHAR_LIMIT)).plus("...")
                else app.name
            applicationSelected.isChecked = wasInitiallyBlacklisted
        }

        viewHolder.itemView.applicationSelected.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked && !wasInitiallyBlacklisted){
                app.addToBlacklistedApps(app.name, app.packageName)
            }else if(!isChecked && wasInitiallyBlacklisted){
                app.removeFromBlacklistedApps(app.packageName)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_application_selection

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount / 1
    }
}