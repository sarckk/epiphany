package com.example.addictionapp.ui.overview

import android.app.usage.UsageStatsManager
import androidx.lifecycle.*
import com.example.addictionapp.data.blocklist.BlocklistRepository
import com.example.addictionapp.utils.ChartModeEnum
import com.example.addictionapp.utils.Constants
import com.github.mikephil.charting.data.Entry
import java.util.*


private const val MS_IN_DAY = 24 * 60 * 60 * 1000

class OverviewViewModel(
    val blocklistRepository: BlocklistRepository
) : ViewModel() {

    private val _overviewChartMode = MutableLiveData(ChartModeEnum.WEEKLY)
    val overViewChartMode : LiveData<ChartModeEnum> = _overviewChartMode
    lateinit var usageStatsManager : UsageStatsManager

    val MILLIS_IN_DAY = 86400000

    fun setStatsManager(usageStatsManager: UsageStatsManager) {
        this.usageStatsManager = usageStatsManager
    }


    fun setChartMode(chartMode: ChartModeEnum){
        _overviewChartMode.value = chartMode
    }

    val chartLineData = _overviewChartMode.switchMap {
        when(it){
            ChartModeEnum.MONTHLY -> getMonthlyData(usageStatsManager)
            ChartModeEnum.WEEKLY -> getWeeklyData(usageStatsManager)
        }
    }

    private val blacklistedPackageNames = blocklistRepository.getAllBlacklistedPackageNames()

    private fun getUsageDataForADay(usageStatsManager: UsageStatsManager, startTime: Long): List<AppUsageData> = usageStatsManager.queryAndAggregateUsageStats(startTime - MILLIS_IN_DAY, startTime).filter {
        blacklistedPackageNames.contains(it.key)
    }.map {
        AppUsageData(blocklistRepository.getBlacklistedAppNameByPackageName(it.key), it.value.totalTimeInForeground / 1000 / 60)
    }

    private fun getPlaceHolderDataForADay(usageStatsManager: UsageStatsManager, daysInPast: Int): PlaceHolderData = PlaceHolderData(Date(System.currentTimeMillis() - daysInPast * MS_IN_DAY), getUsageDataForADay(usageStatsManager, System.currentTimeMillis() - daysInPast * MS_IN_DAY))

    private fun getMonthlyData(usageStatsManager: UsageStatsManager): LiveData<List<Entry>> = liveData {
        /**
        * TODO: PlaceHolderData should be replaced with appropriate data class. Only for demonstration
        */
        val mockEntries = (0..29).map {
            val data = getPlaceHolderDataForADay(usageStatsManager, it)
            Entry(it.toFloat(),
                data.appData.sumByLong { appUsageData -> appUsageData.time }.toFloat(), data)
        }

        emit(mockEntries)
    }

    private fun getWeeklyData(usageStatsManager: UsageStatsManager): LiveData<List<Entry>> = liveData {
        val mockEntries = (0..6).map {
            val data = getPlaceHolderDataForADay(usageStatsManager, it)
            Entry(it.toFloat(),
                data.appData.sumByLong { appUsageData -> appUsageData.time }.toFloat(), data)
        }
        emit(mockEntries)
    }
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}