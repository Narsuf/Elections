package com.jorgedguezm.elections.data.utils

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Party
import com.jorgedguezm.elections.data.models.Result

class ElectionRandomGenerator {

    companion object {

        private fun generateRand() = (0..288).random()
        private fun generateParty(partyId: Long = generateRand().toLong()) = Party(
            id = partyId,
            name = generateRand().toString(),
            color = generateRand().toString()
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

        fun generateElection(): Election {
            val electionId = generateRand().toLong()

            return Election(
                id = electionId,
                name = generateRand().toString(),
                date = generateRand().toString(),
                place = generateRand().toString(),
                chamberName = generateRand().toString(),
                totalElects = generateRand(),
                scrutinized = generateRand().toFloat(),
                validVotes = generateRand(),
                abstentions = generateRand(),
                blankVotes = generateRand(),
                nullVotes = generateRand(),
                results = generateResults(electionId)
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
            val parties = generateParties()

            // Generate 100 results to reduce error margin.
            for (i in 1..100) {
                var result = generateResult(electionId)
                result = result.copy(party = parties.shuffled().take(1)[0])
                results.add(result)
            }

            // Add two duplicated parties with same elects but different votes.
            val result = generateResult(electionId).copy(votes = Int.MAX_VALUE)
            val result2 = result.copy(votes = Int.MIN_VALUE)
            results.add(result)
            results.add(result2)

            return results.distinctBy { it.id }
        }

        private fun generateParties(): List<Party> {
            val parties = mutableListOf<Party>()

            for (i in 1..100) { parties.add(generateParty()) }

            return parties.distinctBy { it.id }
        }
    }
}