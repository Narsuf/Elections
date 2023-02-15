package com.jorgedguezm.elections.data.utils

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Party
import com.jorgedguezm.elections.data.models.Result

class ElectionRandomGenerator {

    companion object {

        private fun generateRand() = (0..288).random()
        private fun generateParty(partyId: Long) = Party(
            id = partyId,
            name = generateRand().toString(),
            color = "672f6c"
        )
        private fun generateResult(electionId: Long): Result {
            val partyId = generateRand().toLong()

            return Result(
                id = generateRand().toLong(),
                partyId = partyId,
                electionId = electionId,
                elects = generateRand(),
                votes = generateRand(),
                party = generateParty(partyId)
            )
        }

        fun generateElection(chamberName: String? = null): Election {
            val chamber = chamberName ?: generateRand().toString()
            val electionId = generateRand().toLong()

            return Election(
                id = electionId,
                name = generateRand().toString(),
                date = generateRand().toString(),
                place = generateRand().toString(),
                chamberName = chamber,
                totalElects = generateRand(),
                scrutinized = generateRand().toFloat(),
                validVotes = generateRand(),
                abstentions = generateRand(),
                blankVotes = generateRand(),
                nullVotes = generateRand(),
                results = listOf(generateResult(electionId))
            )
        }

        fun generateElections(): List<Election> {
            val elections = mutableListOf<Election>()

            // Generate 100 elections to reduce error margin.
            for (i in 1..100) { elections.add(generateElection()) }

            return elections
        }

        fun generateResults(electionId: Long = generateRand().toLong()): List<Result> {
            val results = mutableListOf<Result>()

            // Generate 100 results to reduce error margin.
            for (i in 1..100) { results.add(generateResult(electionId)) }

            // Add two duplicated parties with same elects but different votes.
            val result = generateResult(electionId).copy(votes = Int.MAX_VALUE)
            val result2 = result.copy(votes = Int.MIN_VALUE)
            results.add(result)
            results.add(result2)

            return results
        }
    }
}
