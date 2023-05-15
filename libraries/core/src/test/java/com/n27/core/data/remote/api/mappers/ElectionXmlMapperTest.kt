package com.n27.core.data.remote.api.mappers

import com.n27.core.data.local.JsonReader
import com.n27.test.generators.getElection
import com.n27.test.generators.getParties
import com.n27.test.generators.getParty
import com.n27.test.generators.getResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ElectionXmlMapperTest {

    @Test
    fun `should return expected elections`() = runBlocking {
        val parties = getParties()
        val actual = JsonReader()
            .getStringJson("local-election-test.xml")
            .toElectionXml()
            .toElection(parties)

        val expected = getElection(
            id = 0,
            name = "Ayuntamiento",
            date = "2019",
            place = "Abla",
            chamberName = "Ayuntamiento",
            totalElects = 9,
            validVotes = 817,
            abstentions = 207,
            blankVotes = 4,
            nullVotes = 8,
            results = listOf(
                getResult(
                    id = 0,
                    partyId = 1002824,
                    electionId = 0,
                    elects = 5,
                    votes = 454,
                    party = getParty(id = 1002824)
                ),
                getResult(
                    id = 0,
                    partyId = 1014775,
                    electionId = 0,
                    elects = 4,
                    votes = 351,
                    party = getParty(
                        id = 1014775,
                        name = "PSOE-A",
                        color = actual.results[1].party.color // Color generated randomly
                    )
                )
            )
        )

        assertEquals(expected, actual)
    }
}
