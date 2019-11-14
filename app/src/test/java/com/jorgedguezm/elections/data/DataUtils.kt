package com.jorgedguezm.elections.data

class DataUtils {

    companion object {
        fun generateParty(): Party { return Party("GroenLinks", "#39a935") }

        fun generateElection(results: List<Results>): Election {
            return Election(generateRand().toLong(), "Tweede Kamerverkiezingen",
                    "2016", "Nederland", "Tweede Kamer", generateRand(),
                    generateRand().toFloat(), generateRand(), generateRand(), generateRand(),
                    generateRand(), results)
        }

        fun generateResults(party: Party): Results {
            return Results(generateRand(), generateRand(), party)
        }

        fun generateRand(): Int {
            return (0..288).random()
        }
    }
}