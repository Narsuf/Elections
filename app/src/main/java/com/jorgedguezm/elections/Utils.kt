package com.jorgedguezm.elections

import android.content.Context
import android.net.ConnectivityManager
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.ui.detail.DetailFragment

import kotlinx.android.synthetic.main.detail_activity.*

import java.math.BigDecimal
import java.math.RoundingMode

import javax.inject.Inject

class Utils @Inject constructor(private val context: Context) {

    fun isConnectedToInternet(): Boolean {
        var isConnected = false
        val connectivity = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivity.run {
            activeNetworkInfo?.let { isConnected = it.isConnected }
        }

        return isConnected
    }

    fun drawPieChart(chart: PieChart, results: List<Results>) {
        val elects = getElectsFromResults(results)
        val colors = getColorsFromResults(results)

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

    private fun getElectsFromResults(results: List<Results>): Array<Int> {
        val elects = ArrayList<Int>()

        for (r in results) elects.add(r.elects)

        return elects.toTypedArray()
    }

    private fun getColorsFromResults(results: List<Results>): Array<String> {
        val colors = ArrayList<String>()

        for (r in results) colors.add("#" + r.party.color)

        return colors.toTypedArray()
    }

    fun getPercentageWithTwoDecimals(dividend: Int, divisor: Int): BigDecimal {
        val percentage = (dividend.toDouble() / divisor) * 100
        return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    }

    fun beginTransactionToDetailFragment(activity: AppCompatActivity, election: Election) {
        val detailFragment = DetailFragment()
        val transaction = activity.supportFragmentManager.beginTransaction()

        detailFragment.arguments = Bundle().apply {
            putSerializable(Constants.KEY_ELECTION, election)
        }

        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
        activity.toolbar.title = generateToolbarTitle(election)
    }

    fun generateToolbarTitle(election: Election): String {
        return election.chamberName + " (" + election.date + ")"
    }
}