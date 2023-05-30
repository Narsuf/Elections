package com.n27.core.data.local.room.mappers

import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionRaw
import org.junit.Assert.assertEquals
import org.junit.Test

class ElectionRawMapperTest {

    @Test
    fun `should return expected electionRaw`() {
        val expected = getElectionRaw()
        val actual = getElection().toElectionRaw()

        assertEquals(expected, actual)
    }
}