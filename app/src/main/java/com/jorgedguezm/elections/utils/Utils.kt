package com.jorgedguezm.elections.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.graphics.Color
import android.graphics.Color.TRANSPARENT

import javax.inject.Inject

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry

import com.jorgedguezm.elections.data.Results

class Utils @Inject constructor(private val context: Context) {

    fun isConnectedToInternet(): Boolean {
        val connectivity = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivity != null) {
            val info  = connectivity.allNetworkInfo

            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) return true
                }
            }
        }

        return false
    }

    fun drawPieChart(chart: PieChart, elects: Array<Int>, colors: Array<String>) {
        chart.description = null
        chart.legend.isEnabled = false

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(TRANSPARENT)
        chart.holeRadius = 60F

        val yVals = ArrayList<PieEntry>()

        for (i in elects.indices)
            yVals.add(PieEntry(elects[i].toFloat(), i.toFloat()))

        val dataSet = PieDataSet(yVals, null)
        dataSet.sliceSpace = 0.8F

        val colorsArray = ArrayList<Int>()

        for (one in colors)
            colorsArray.add(Color.parseColor(one))

        dataSet.colors = colorsArray

        val data = PieData(dataSet)
        data.setValueTextSize(0f)

        chart.data = data
        chart.maxAngle = 180F
        chart.rotationAngle = 180F
        chart.setTouchEnabled(false)
        chart.animateXY(1500, 1500)
    }

    fun getElectsFromResults(results: List<Results>): Array<Int> {
        val elects = ArrayList<Int>()

        for (r in results)
            elects.add(r.elects!!)

        return elects.toTypedArray()
    }

    fun getColorsFromResults(results: List<Results>,
                                     partiesColor: Map<String, String>): Array<String> {

        val colors = ArrayList<String>()

        for (r in results)
            colors.add("#" + partiesColor[r.partyId])

        return colors.toTypedArray()
    }
}