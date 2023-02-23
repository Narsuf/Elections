package org.n27.elections.data

import org.n27.elections.data.utils.getElection
import org.n27.elections.data.utils.getElectionWithResultsAndParty
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
