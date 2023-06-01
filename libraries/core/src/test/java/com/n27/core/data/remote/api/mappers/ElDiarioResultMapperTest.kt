package com.n27.core.data.remote.api.mappers

import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result
import com.n27.core.domain.live.models.LiveElection
import com.n27.test.generators.getElDiarioParties
import com.n27.test.generators.getElDiarioPartyResult
import com.n27.test.generators.getElDiarioResult
import com.n27.test.generators.getElection
import com.n27.test.generators.getLiveElection
import com.n27.test.generators.getParties
import com.n27.test.generators.getParty
import com.n27.test.generators.getResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElDiarioResultMapperTest {

    @Test
    fun `should return expected ElDiarioLocalResult`() = runBlocking {
        val actual = JsonReader()
            .getStringJson("local-election-test.json")
            .toElDiarioLocalResult("04001", 2305)

        val expected = getElDiarioResult(
            partiesResults = listOf(
                getElDiarioPartyResult(),
                ElDiarioPartyResult(id = "0006", votes = 347, percentage = 4443, seats = 4)
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected ElDiarioRegionalResult`() = runBlocking {
        val actual = JsonReader()
            .getStringJson("local-election-test.json")
            .toElDiarioRegionalResult("04001", 2305)

        val expected = getElDiarioResult(
            partiesResults = listOf(
                getElDiarioPartyResult(),
                ElDiarioPartyResult(id = "0006", votes = 347, percentage = 4443, seats = 4)
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected LiveElection`() = runBlocking {
        val actual = JsonReader()
            .getStringJson("local-election-test.json")
            .toElDiarioRegionalResult("04001", 2305)
            .toLiveElection("Municipales", "Abla", getElDiarioParties())

        val expected = LiveElection(
            id = "04001",
            Election(
                id = 0,
                name = "Municipales",
                date = "05/23",
                place = "Abla",
                chamberName = "",
                totalElects = 9,
                scrutinized = 100.0f,
                validVotes = 800,
                abstentions = 215,
                blankVotes = 7,
                nullVotes = 19,
                results = listOf(
                    Result(
                        id = 0,
                        partyId = 30,
                        electionId = 0,
                        elects = 5,
                        votes = 411,
                        party = Party(id = 30, name = "PSOE", color = "#E02020")
                    ),
                    Result(
                        id = 0,
                        partyId = 6,
                        electionId = 0,
                        elects = 4,
                        votes = 347,
                        party = Party(id = 6, name = "PP", color = "#02A2DD")
                    ),
                )
            )
        )

        assertEquals(expected, actual)
    }
}
