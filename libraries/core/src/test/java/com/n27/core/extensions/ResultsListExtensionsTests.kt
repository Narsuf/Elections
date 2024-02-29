package com.n27.core.extensions

import com.n27.test.generators.getParty
import com.n27.test.generators.getResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultsListExtensionsTests {

    @Test
    fun getElects() {
        val results = listOf(
            getResult(),
            getResult(elects = 2)
        )

        val chartData = results.getPieChartData()

        chartData.slices.forEachIndexed { index, slice ->
            assertEquals(results[index].elects.toFloat(), slice.value)
        }
    }

    @Test
    fun getColors() {
        val results = listOf(
            getResult(),
            getResult(
                party = getParty(
                    color = "FFFFFF"
                )
            )
        )

        val chartData = results.getPieChartData()

        chartData.slices.forEachIndexed { index, slice ->
            assertEquals("#${results[index].party.color}", slice.color)
        }
    }
}
