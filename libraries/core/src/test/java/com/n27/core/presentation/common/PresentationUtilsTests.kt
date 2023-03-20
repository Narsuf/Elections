package com.n27.core.presentation.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class PresentationUtilsTests {

    @Test
    fun getPercentageWithTwoDecimals() {
        val utils = PresentationUtils()
        val partyVotes = 1
        val totalVotes = 3

        assertEquals(
            utils.getPercentageWithTwoDecimals(partyVotes, totalVotes),
            BigDecimal(33.33).setScale(2, RoundingMode.HALF_UP)
        )
    }
}
