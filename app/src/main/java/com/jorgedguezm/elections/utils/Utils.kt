package com.jorgedguezm.elections.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.net.ConnectivityManager
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.models.Results
import com.jorgedguezm.elections.view.ui.detail.DetailActivity
import com.jorgedguezm.elections.view.ui.detail.DetailFragment
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

    // UI related.
    fun generateToolbarTitle(election: Election): String {
        return election.chamberName + " (" + election.date + ")"
    }

    fun beginTransactionToDetailFragment(activity: DetailActivity, election: Election) {
        val detailFragment = DetailFragment()
        val transaction = activity.supportFragmentManager.beginTransaction()

        detailFragment.arguments = Bundle().apply {
            putSerializable(Constants.KEY_ELECTION, election)
        }

        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
        activity.binding.toolbar.title = generateToolbarTitle(election)
    }

    fun drawPieChart(chart: PieChart, results: List<Results>) {
        val sortedResults = results.sortedByDescending { it.elects }

        val elects = getElectsFromResults(sortedResults)
        val colors = getColorsFromResults(sortedResults)

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

    // Logical functions.
    fun getPercentageWithTwoDecimals(dividend: Int, divisor: Int): BigDecimal {
        val percentage = (dividend.toDouble() / divisor) * 100
        return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    }

    fun getPercentageData(election: Election): Array<String> {
        val census = election.validVotes + election.abstentions

        val percentageOfParticipation = getPercentageWithTwoDecimals(election.validVotes, census)
        val percentageOfAbstentions = getPercentageWithTwoDecimals(election.abstentions, census)
        val percentageOfNull = getPercentageWithTwoDecimals(election.nullVotes, election.validVotes)
        val percentageOfBlank = getPercentageWithTwoDecimals(election.blankVotes, election.validVotes)

        return arrayOf(election.scrutinized.toString(), "", percentageOfParticipation.toString(),
                percentageOfAbstentions.toString(), percentageOfNull.toString(),
                percentageOfBlank.toString())
    }

    fun sortElections(elections: List<Election>): List<Election> {
        val electionsCopy = elections.map { it.copy() }

        electionsCopy.sortedByDescending {
            if (it.date.length > 4)
                it.date.toDouble() / 10
            else
                it.date.toDouble()
        }.let { sortedElections ->
            sortedElections.forEach {
                if (it.date.length > 4) {
                    it.date = when (it.date) {
                        "20192" -> "2019-10N"
                        "20191" -> "2019-28A"
                        else -> it.date
                    }
                }
            }

            return sortedElections
        }
    }
}