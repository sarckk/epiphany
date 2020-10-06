package com.example.addictionapp.ui.overview

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DayOfWeekFormatter : ValueFormatter() {
    private val daysOfWeek = listOf("MO", "TU", "WE", "TH", "FR","SA","SU")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return daysOfWeek.getOrElse(value.toInt()) { value.toString() }
    }
}