package com.example.addictionapp.ui.overview

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.addictionapp.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment : Fragment(), OnChartValueSelectedListener {
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

        setUpToolbar()
        setUpLineChart()

        // TODO: Remove before production
        test.setOnClickListener {
            findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToReflectionListFragment(null))
        }
    }

    private fun setUpToolbar() {
        overviewToolbarText.setText(R.string.app_name)
        overviewToolbar.inflateMenu(R.menu.menu_overview)
    }

    private fun setUpMockData(): LineData {
        // TODO: Move this conditional to actual implementation
        /**
        if(overviewChart.data?.dataSetCount!! > 0){
            lineDataSet = overviewChart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.setValues(newValues)
            lineDataSet.notifyDataSetChanged()
            overviewChart.data.notifyDataChanged()
            overviewChart.notifyDataSetChanged()
        }
        **/
        // TODO: Replace with actual db data before production
        // TODO: Add license for MPAndroidChart
        val mockEntries = (0..6).map {
            Entry(it.toFloat(), (1..100).random().toFloat())
        }

        val lineDataSet = LineDataSet(mockEntries, "mockLineDataSet").apply {
            setDrawCircleHole(true)
            lineWidth = 1.3f
            setDrawValues(false)
            color = R.color.epiphanyPurple
            setCircleColor(R.color.epiphanyPurple)
            mode = LineDataSet.Mode.CUBIC_BEZIER

            // gradient fill
            setDrawFilled(true)
            if(Utils.getSDKInt() >= 18){
                val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.graph_fade)
                fillDrawable = gradientDrawable
            } else{
                fillColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            }
        }
        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(lineDataSet)
        return LineData(dataSets)
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

            // xAxis
            xAxis.isEnabled = true
            xAxis.yOffset = 10f
            xAxis.setDrawAxisLine(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = DayOfWeekFormatter()

            description.isEnabled = false
            legend.isEnabled = false

            // lastly, set data
            data = setUpMockData()

            // animate calls invalidate()
            animateY(1000)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.d(TAG, e.toString())
        Log.d(TAG, h.toString())
    }

    override fun onNothingSelected() {
        Log.d(TAG, "Nothing selected")
    }
}