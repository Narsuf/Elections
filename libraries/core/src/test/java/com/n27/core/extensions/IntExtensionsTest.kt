package com.n27.core.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class IntExtensionsTest {

    @Test
    fun toStringId() {
        assertEquals("01", 1.toStringId())
        assertEquals("10", 10.toStringId())
    }

    @Test
    fun divide() {
        val partyVotes = 1
        val totalVotes = 3

        assertEquals(
            partyVotes.divide(totalVotes),
            BigDecimal(33.33).setScale(2, RoundingMode.HALF_UP).toString()
        )
    }
}
