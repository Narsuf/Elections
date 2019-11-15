package com.jorgedguezm.elections.data

class DataUtils {

    companion object {
        fun generateRand(): Int { return (0..288).random() }

        fun generateParty(): Party {
            return Party(generateRand().toString(), generateRand().toString())
        }

        fun generateResults(): Results {
            return Results(generateRand(), generateRand(), generateParty())
        }

        fun generateElection(): Election {
            return Election(generateRand().toLong(), generateRand().toString(),
                    generateRand().toString(), generateRand().toString(), generateRand().toString(),
                    generateRand(), generateRand().toFloat(), generateRand(), generateRand(),
                    generateRand(), generateRand(), arrayListOf(generateResults()))
        }
    }
}