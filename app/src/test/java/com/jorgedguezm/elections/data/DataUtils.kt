package com.jorgedguezm.elections.data

class DataUtils {

    companion object {
        fun generateParty(): Party { return Party("GroenLinks", "#39a935") }

        fun generateElection(): Election {
            return Election(generateRand().toLong(), "Tweede Kamerverkiezingen",
                    generateRand(), "Nederland", "Tweede Kamer", generateRand(),
                    generateRand().toFloat(), generateRand(), generateRand(), generateRand(),
                    generateRand())
        }

        fun generateResults(party: Party, election: Election): Results {
            return Results(generateRand().toLong(), generateRand(), generateRand(), party,
                    election.id)
        }

        fun generateStoredCongressElection(): Election {
            return Election(generateRand().toLong(), "Generales", 2016, "España",
                    "Congreso", generateRand(), generateRand().toFloat(),
                    generateRand(), generateRand(), generateRand(), generateRand())
        }

        fun generateStoredSenateElection(): Election {
            return Election(generateRand().toLong(), "Generales", 2016, "España",
                    "Senado", generateRand(), generateRand().toFloat(), generateRand(),
                    generateRand(), generateRand(), generateRand())
        }

        fun generateRand(): Int {
            return (0..288).random()
        }
    }
}