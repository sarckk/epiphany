package com.example.addictionapp.ui.overview

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DayFormatter: ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return SimpleDateFormat("EEE").format(Date(value.toLong()))
    }
}
