package com.example.addictionapp.ui.overview

import androidx.lifecycle.*
import com.example.addictionapp.utils.ChartModeEnum
import com.example.addictionapp.utils.Constants
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import java.util.*


private const val MS_IN_DAY = 24 * 60 * 60 * 1000

class OverviewViewModel() : ViewModel() {

    private val _overviewChartMode = MutableLiveData(ChartModeEnum.WEEKLY)
    val overViewChartMode : LiveData<ChartModeEnum> = _overviewChartMode

   fun setChartMode(chartMode: ChartModeEnum){
       _overviewChartMode.value = chartMode
   }

   val chartLineData = _overviewChartMode.switchMap {
       when(it){
           ChartModeEnum.MONTHLY -> getMonthlyData()
           ChartModeEnum.WEEKLY -> getWeeklyData()
       }
   }

   private fun getMonthlyData(): LiveData<List<Entry>> = liveData {
       /**
        * TODO: PlaceHolderData should be replaced with appropriate data class. Only for demonstration
        */
       val mockEntries = (0..29).map {
           Entry(it.toFloat(), (1..100).random().toFloat(),
               PlaceHolderData(Date(System.currentTimeMillis() - it * MS_IN_DAY),
                   com.example.addictionapp.data.models.Reflection("test - ${it}", Constants.PRETTY_BAD, "test")))
       }

       emit(mockEntries)
   }

    private fun getWeeklyData(): LiveData<List<Entry>> = liveData {
        val mockEntries = (0..6).map {
            Entry(it.toFloat(), (1..100).random().toFloat(),
                PlaceHolderData(Date(System.currentTimeMillis() - it * MS_IN_DAY),
                    com.example.addictionapp.data.models.Reflection("test - ${it}", Constants.PRETTY_BAD, "test")))
        }
        emit(mockEntries)
    }
}