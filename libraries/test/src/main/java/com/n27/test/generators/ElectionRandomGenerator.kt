package com.n27.test.generators

import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes

class ElectionRandomGenerator {

    companion object {

        private fun generateRand() = (1..Int.MAX_VALUE).random()

        private fun generateParty() = Party(
            id = generateRand().toLong(),
            name = generateRand().toString(),
            color = generateRand().toString()
        )

        fun generateResult(
            electionId: Long = generateRand().toLong(),
            party: Party = generateParty()
        ) = Result(
            id = generateRand().toLong(),
            partyId = party.id,
            electionId = electionId,
            elects = generateRand(),
            votes = generateRand(),
            party = party
        )

        fun generateElection(results: List<Result> = generateResults()) = Election(
            id = results[0].electionId,
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
            results = results
        )

        fun generateElections(): List<Election> {
            val elections = mutableListOf<Election>()
            val parties = generateParties()

            for (i in 0..100) {
                generateElection(
                    results = generateResults(
                        parties = parties.shuffled().take(10)
                    )
                ).let { elections.add(it) }
            }

            return elections
                .distinctBy { it.id }
                .distinctBy { e -> e.results.forEach { it.id } }
                .map { it.sortResultsByElectsAndVotes() }
                .sortByDateAndFormat()
        }

        fun generateResults(parties: List<Party> = generateParties()): List<Result> {
            val results = mutableListOf<Result>()
            val electionId = generateRand().toLong()

            parties.forEach { party ->
                generateResult(electionId, party).let { results.add(it) }
            }

            return results.distinctBy { it.id }
        }

        fun generateParties(): List<Party> {
            val parties = mutableListOf<Party>()

            for (i in 0..100) { parties.add(generateParty()) }

            return parties.distinctBy { it.id }
        }
    }
}
