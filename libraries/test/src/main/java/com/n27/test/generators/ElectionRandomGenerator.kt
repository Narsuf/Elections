package com.n27.test.generators

import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result

class ElectionRandomGenerator {

    companion object {

        private fun generateRand() = (0..288).random()
        private fun generateParty(partyId: Long = generateRand().toLong()) = Party(
            id = partyId,
            name = generateRand().toString(),
            color = generateRand().toString()
        )
        fun generateResult(electionId: Long = generateRand().toLong()): Result {
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
            val results = generateResults()

            // Generate 100 elections to reduce error margin.
            for (i in 1..100) {
                // This black magic here is needed to maintain healthy references.
                val election = generateElection().copy(results = results.shuffled().take(10))
                elections.add(election)
            }

            return elections
        }

        fun generateResults(electionId: Long = generateRand().toLong()): List<Result> {
            val results = mutableListOf<Result>()
            val parties = generateParties()

            for (i in 1..1000) {
                val result = generateResult(electionId)
                    .copy(party = parties.shuffled().take(1)[0])
                results.add(result)
            }

            return results.distinctBy { it.id }
        }

        private fun generateParties(): List<Party> {
            val parties = mutableListOf<Party>()

            for (i in 1..600) { parties.add(generateParty()) }

            return parties.distinctBy { it.id }
        }
    }
}
