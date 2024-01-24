package com.n27.core.extensions

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.n27.core.domain.election.models.Result

fun PieChart.drawWithResults(results: List<Result>) {
    val elects = results.getElects()
    val colors = results.getColors()

    description = null
    legend.isEnabled = false
    isDrawHoleEnabled = true
    setHoleColor(Color.TRANSPARENT)
    holeRadius = 60F

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

    setData(data)
    maxAngle = 180F
    rotationAngle = 180F
    setTouchEnabled(false)
    animateXY(1500, 1500)
}