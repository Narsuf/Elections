package com.jorgedguezm.elections.data

class DataUtils {

    companion object {
        fun generateRand(): Int { return (0..288).random() }

        fun generateParty(): Party {
            return Party(generateRand().toString(), "672f6c")
        }

        fun generateResults(): Results {
            return Results(generateRand(), generateRand(), generateParty())
        }

        fun generateElection(chamberName: String? = null): Election {
            val chamber = chamberName ?: generateRand().toString()

            return Election(generateRand().toLong(), generateRand().toString(),
                    generateRand().toString(), generateRand().toString(), chamber, generateRand(),
                    generateRand().toFloat(), generateRand(), generateRand(), generateRand(),
                    generateRand(), arrayListOf(generateResults()))
        }
    }
}