package com.n27.core.data.local.room.mappers

import com.n27.test.generators.getResult
import com.n27.test.generators.getResultWithParty
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultWithPartyMapperTest {

    @Test
    fun `should return expected resultWithParty`() {
        val expected = getResultWithParty()
        val actual = getResult().toResultWithParty()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected result`() {
        val expected = getResult()
        val actual = getResultWithParty().toResult(3)

        assertEquals(expected, actual)
    }
}