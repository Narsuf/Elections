package com.n27.elections.domain.mappers

import com.n27.core.Constants.KEY_SENATE
import com.n27.elections.domain.models.GeneralElections
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getGeneralElection
import org.junit.Assert.assertEquals
import org.junit.Test

class GeneralElectionsMapperTest {

    @Test
    fun `should return expected electionRaw`() {
        val senateElections = listOf(getElection(chamberName = KEY_SENATE))

        val actual = getGeneralElection()
        val expected = GeneralElections(getElectionList(), senateElections)

        assertEquals(expected, actual.toGeneralElections())
    }
}