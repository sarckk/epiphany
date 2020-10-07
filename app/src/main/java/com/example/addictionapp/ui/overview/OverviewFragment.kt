package com.example.addictionapp.ui.overview

import android.app.Notification
import android.app.PendingIntent
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.ReflectionRepository
import com.example.addictionapp.data.models.ApplicationWithIcon
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.ui.apps.AppSelectionItem
import com.example.addictionapp.ui.reflection.list.ReflectionListViewModel
import com.example.addictionapp.utils.ChartModeEnum
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_overview.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class OverviewFragment : Fragment(), OnChartValueSelectedListener {
    private val viewModel by viewModel<OverviewViewModel>()

    companion object {
       private const val TAG = "OverviewFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setStatsManager(context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager)
        setUpRadioListeners()
        setUpToolbar()
        setUpLineChart()
        bindChartToViewModel()
    }

    private fun setUpRadioListeners(){
        radioGrp.setOnCheckedChangeListener { _, checkedId ->
            val chartMode = when(checkedId){
                R.id.radioWeekly -> ChartModeEnum.WEEKLY
                R.id.radioMonthly ->  ChartModeEnum.MONTHLY
                else -> ChartModeEnum.WEEKLY
            }
            viewModel.setChartMode(chartMode)
        }
    }

    private fun bindChartToViewModel(){
        viewModel.chartLineData.observe(viewLifecycleOwner, Observer {
            if(it != null){
              setNewData(it, viewModel.overViewChartMode.value!!)
            }
        })
    }

    private fun setUpToolbar() {
        overviewToolbarText.setText(R.string.app_name)
        overviewToolbar.inflateMenu(R.menu.menu_overview)
    }

    private fun setNewData (newValues: List<Entry>, chartMode: ChartModeEnum) {
        // TODO: Add license for MPAndroidChart
        var lineDataSet: LineDataSet

        if(overviewChart.data != null && overviewChart.data.dataSetCount > 0){
            lineDataSet = overviewChart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet = setLineDataSetStyle(lineDataSet,  chartMode)
            lineDataSet.values = newValues
            lineDataSet.notifyDataSetChanged()
            setChartXAXisStyle(chartMode)

            if(overviewChart.highlighted != null && overviewChart.highlighted[0] != null){
               overviewChart.highlightValue(overviewChart.highlighted[0], true)
            } else{
               overviewChart.highlightValue(newValues[0].x,newValues[0].y,0,true)
            }

            overviewChart.data.notifyDataChanged()
            overviewChart.notifyDataSetChanged()
        } else {
            lineDataSet = LineDataSet(newValues, "mock${chartMode}Data")
            lineDataSet= setLineDataSetStyle(lineDataSet, chartMode)
            val dataSets = arrayListOf<ILineDataSet>()
            dataSets.add(lineDataSet)
            setChartXAXisStyle(chartMode)
            overviewChart.data = LineData(dataSets)

            overviewChart.highlightValue(newValues[0].x, newValues[0].y, 0)
        }
        overviewChart.animateY(800)
    }

    private fun setLineDataSetStyle(lineDataSet: LineDataSet, chartMode: ChartModeEnum): LineDataSet{
        return lineDataSet.apply{
            setDrawCircleHole(true)
            lineWidth = 1.3f
            setDrawValues(false)
            color = R.color.epiphanyPurple
            setCircleColor(R.color.epiphanyPurple)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawHorizontalHighlightIndicator(false)
            highlightLineWidth = 1.2f

            // gradient fill
            setDrawFilled(true)
            if(Utils.getSDKInt() >= 18){
                val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.graph_fade)
                fillDrawable = gradientDrawable
            } else{
                fillColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            }
        }
    }

    private fun setChartXAXisStyle( chartMode: ChartModeEnum ){
        if(chartMode == ChartModeEnum.WEEKLY){
            overviewChart.xAxis.run {
                valueFormatter = DayOfWeekFormatter()
                setLabelCount(7,true)
            }
        } else {
            overviewChart.xAxis.run {
                valueFormatter = null
                setLabelCount(5,true)
            }
        }
    }

    private fun setUpLineChart(){
        overviewChart.run {
            setTouchEnabled(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            isDragEnabled= true
            setOnChartValueSelectedListener(this@OverviewFragment)

            setDrawGridBackground(false)
            setDrawBorders(false)
            axisLeft.isEnabled = false
            axisRight.isEnabled = false

            description.isEnabled = false
            legend.isEnabled = false

            // xAxis
            xAxis.run {
                isEnabled = true
                yOffset = 2f
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }

            // animate calls invalidate() internally
            animateY(800)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val data = e?.data as PlaceHolderData?
        overviewInfoDate.text = formatter.format(data?.date)
        // this should be a link to the reflection using navigation component and passing in the primary key as argument
        data?.appData?.let { updateRecycler(it) }
    }

    private fun updateRecycler(applications: List<AppUsageData>) {
        val applicationListItems = applications.toApplicationItem()

        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 1

            addAll(applicationListItems)
        }

        applicationUsageRecyclerView.apply {
            layoutManager = GridLayoutManager(context, groupieAdapter.spanCount).apply{
                spanSizeLookup = groupieAdapter.spanSizeLookup
            }
            adapter = groupieAdapter
        }
    }

    override fun onNothingSelected() {
        Log.d(TAG, "Nothing selected")
    }

    private fun List<AppUsageData>.toApplicationItem() : List<AppUsageItem> {
        return this.map { application ->
            AppUsageItem(application)
        }
    }
}