package com.jorgedguezm.elections.data.utils

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Party
import com.jorgedguezm.elections.data.models.Result

class ElectionGenerator {

    companion object {

        private fun generateRand() = (0..288).random()
        private fun generateParty() = Party(generateRand().toString(), "672f6c")
        private fun generateResult() = Result(generateRand(), generateRand(), generateParty())

        fun generateElection(chamberName: String? = null): Election {
            val chamber = chamberName ?: generateRand().toString()

            return Election(
                id = generateRand().toLong(),
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
                results = listOf(generateResult())
            )
        }

        fun generateElections(): List<Election> {
            val elections = mutableListOf<Election>()

            // Generate 100 elections to reduce error margin.
            for (i in 1..100) { elections.add(generateElection()) }

            return elections
        }

        fun generateResults(): List<Result> {
            val results = mutableListOf<Result>()

            // Generate 100 results to reduce error margin.
            for (i in 1..100) { results.add(generateResult()) }

            // Add two duplicated parties with same elects but different votes.
            val result = generateResult().copy(votes = Int.MAX_VALUE)
            val result2 = result.copy(votes = Int.MIN_VALUE)
            results.add(result)
            results.add(result2)

            return results
        }
    }
}
