package com.jorgedguezm.elections.data

class DataUtils {

    companion object {
        fun generateParty() : Party { return Party("GroenLinks", "#39a935") }

        fun generateElection() : Election {
            val rand = (0..288).random()
            return Election(rand.toLong(), "Tweede Kamerverkiezingen", rand, "Nederland",
                    "Tweede Kamer", rand, rand.toFloat(), rand, rand, rand, rand)
        }

        fun generateStoredCongressElection() : Election {
            val rand = (0..288).random()
            return Election(rand.toLong(), "Generales", 2016, "España",
                    "Congreso", rand, rand.toFloat(), rand, rand, rand, rand)
        }

        fun generateStoredSenateElection() : Election {
            val rand = (0..288).random()
            return Election(rand.toLong(), "Generales", 2016, "España",
                    "Senado", rand, rand.toFloat(), rand, rand, rand, rand)
        }
    }
}