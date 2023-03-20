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

        val elects = results.getElects()

        elects.forEachIndexed { index, elect ->
            assertEquals(results[index].elects, elect)
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

        val colors = results.getColors()

        colors.forEachIndexed { index, color ->
            assertEquals("#${results[index].party.color}", color)
        }
    }
}
