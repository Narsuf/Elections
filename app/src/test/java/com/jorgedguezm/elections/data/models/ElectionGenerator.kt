package com.jorgedguezm.elections.data.models

class ElectionGenerator {

    companion object {

        private fun generateRand() = (0..288).random()
        private fun generateParty() = Party(generateRand().toString(), "672f6c")
        private fun generateResults() = Results(generateRand(), generateRand(), generateParty())

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
                results = arrayListOf(generateResults())
            )
        }

        fun generateElections(): List<Election> {
            val elections = mutableListOf<Election>()

            // Generate 100 elections to reduce error margin.
            for (i in 1..100) { elections.add(generateElection()) }

            return elections
        }
    }
}
