package com.n27.core.data.remote.api.mappers

import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result
import com.n27.core.domain.live.models.LiveElection
import com.n27.test.generators.getElDiarioParties
import com.n27.test.generators.getElDiarioResult
import com.n27.test.jsons.ElDiarioApiResponses
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElDiarioResultMapperTest {

    @Test
    fun `should return expected ElDiarioResult`() = runBlocking {
        val expected = ElDiarioResult(
            id = "",
            date = 2305,
            abstentions = 10506203,
            blankVotes = 216515,
            census = 34872054,
            scrutinized = 10000,
            nullVotes = 249499,
            validVotes = 24365851,
            seats = 350,
            partiesResults = listOf(
                ElDiarioPartyResult(id = "0094", votes = 6752983, percentage = 2800, seats = 120),
                ElDiarioPartyResult(id = "0083", votes = 5019869, percentage = 2082, seats = 88)
            )
        )

        val actual = ElDiarioApiResponses.congressElection.toElDiarioResult(2305, 350)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected ElDiarioRegionalResult`() = runBlocking {
        val actual = ElDiarioApiResponses.regionalElection.toElDiarioRegionalResult("02", 2305)

        val expected = ElDiarioResult(
            id = "02",
            date = 2305,
            abstentions = 299185,
            blankVotes = 10609,
            census = 979418,
            scrutinized = 9820,
            nullVotes = 8144,
            validVotes = 662691,
            seats = 67,
            partiesResults = listOf(
                ElDiarioPartyResult(id = "2659", votes = 20310, percentage = 310, seats = 1)
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `should return expected ElDiarioLocalResult`() = runBlocking {
        val actual = ElDiarioApiResponses.localElection.toElDiarioLocalResult("04001", 2305)

        assertEquals(getElDiarioResult(), actual)
    }

    @Test
    fun `should return expected LiveElection`() = runBlocking {
        val actual = ElDiarioApiResponses.localElection
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
                    )
                )
            )
        )

        assertEquals(expected, actual)
    }
}
