package com.example.addictionapp.ui.overview

import java.util.*

data class AppUsageData(
   val name: String,
   val time: Long
)

data class PlaceHolderData(
   val date: Date,
   val appData: List<AppUsageData>
)