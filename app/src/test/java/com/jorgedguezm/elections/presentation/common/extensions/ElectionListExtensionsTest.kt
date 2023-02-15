package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Result
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateElection
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateElections
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateResults
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ElectionListExtensionsTest {

    @Test
    fun sortElections() {
        val elections = generateElections()
        val sortedElections = elections.sortByDate()
        var lastElection: Election? = null

        sortedElections.forEach {
            val currentDate = it.date.toInt()
            val lastDate = lastElection?.date?.toInt() ?: Int.MAX_VALUE

            assertTrue(currentDate <= lastDate)

            lastElection = it
        }
    }

    @Test
    fun sortResults() {
        val election = listOf(generateElection().copy(results = generateResults()))
        val sortedElection = election.sortResultsByElectsAndVotes()
        var lastResult: Result? = null

        sortedElection[0].results.forEach {
            val currentElects = it.elects
            val currentVotes = it.votes
            val lastElects = lastResult?.elects ?: Int.MAX_VALUE
            val lastVotes = lastResult?.votes ?: Int.MAX_VALUE

            assertTrue(currentElects <= lastElects)
            if (currentElects == lastElects) assertTrue(currentVotes <= lastVotes)

            lastResult = it
        }
    }
}
