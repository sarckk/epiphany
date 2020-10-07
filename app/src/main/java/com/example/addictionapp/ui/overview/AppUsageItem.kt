package com.example.addictionapp.ui.overview

import android.annotation.SuppressLint
import com.example.addictionapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_application_usage.view.*

class AppUsageItem(private val applicationUsageData: AppUsageData) : Item(){

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply{
            applicationUsageName.text = applicationUsageData.name
            applicationUsageTime.text = applicationUsageData.time.toString() + " minutes"
        }
    }

    override fun getLayout(): Int = R.layout.item_application_usage

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount / 1
    }
}