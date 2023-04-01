package com.n27.core.data.local.room.mappers

import com.n27.test.generators.getElections
import com.n27.test.generators.getElectionsWithResultsAndParty
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class ElectionWithResultsAndPartyMapperTest {

    @Test
    fun `should return expected election`() {
        val expected = getElections()
        val actual = getElectionsWithResultsAndParty().toElections()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected electionWithResultsAndParty`() {
        val expected = getElectionsWithResultsAndParty()
        val actual = getElections().toElectionsWithResultsAndParty()

        assertEquals(expected, actual)
    }
}
