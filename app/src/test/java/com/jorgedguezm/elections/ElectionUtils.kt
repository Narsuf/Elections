package com.jorgedguezm.elections

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party

class ElectionUtils {

    companion object {
        fun generateParty() : Party {
            return Party("GroenLinks", "#39a935")
        }

        fun generateElection() : Election {
            val rand = (0..288).random()
            return Election(rand.toLong(), "Tweede Kamerverkiezingen", rand,
                    "Nederland", "Tweede Kamer", rand, rand.toFloat(), rand,
                    rand, rand, rand)
        }
    }
}