package com.n27.core.data.mappers

import com.n27.core.data.room.mappers.toElection
import com.n27.core.data.room.mappers.toElectionWithResultsAndParty
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionWithResultsAndParty
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class ElectionMapperTest {

    @Test
    fun `should return expected election`() {
        val expected = getElection()
        val actual = getElectionWithResultsAndParty().toElection()

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected electionWithResultsAndParty`() {
        val expected = getElectionWithResultsAndParty()
        val actual = getElection().toElectionWithResultsAndParty()

        assertEquals(expected, actual)
    }
}
