package com.n27.core.presentation.common

import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.n27.core.BuildConfig
import com.n27.core.data.models.Result
import com.n27.core.presentation.common.extensions.getColors
import com.n27.core.presentation.common.extensions.getElects
import java.math.BigDecimal
import java.math.RoundingMode

open class PresentationUtils {

    fun drawPieChart(chart: PieChart, results: List<Result>) {
        val elects = results.getElects()
        val colors = results.getColors()

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

    fun getPercentageWithTwoDecimals(dividend: Int, divisor: Int): BigDecimal {
        val percentage = (dividend.toDouble() / divisor) * 100
        return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    }

    inline fun track(eventName: String, crossinline block: ParametersBuilder.() -> Unit = {}) {
        if (!BuildConfig.DEBUG) Firebase.analytics.logEvent(eventName, block)
    }
}