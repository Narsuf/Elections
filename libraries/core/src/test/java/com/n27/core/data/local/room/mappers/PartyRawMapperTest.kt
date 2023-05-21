package com.n27.core.data.local.room.mappers

import com.n27.test.generators.getParties
import com.n27.test.generators.getPartiesRaw
import com.n27.test.generators.getParty
import com.n27.test.generators.getPartyRaw
import org.junit.Assert.assertEquals
import org.junit.Test

class PartyRawMapperTest {

    @Test
    fun `should return expected parties`() {
        val expected = getParties()
        val actual = getPartiesRaw().toParties()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected partieRaw`() {
        val expected = getPartyRaw()
        val actual = getParty().toPartyRaw()

        assertEquals(expected, actual)
    }
}