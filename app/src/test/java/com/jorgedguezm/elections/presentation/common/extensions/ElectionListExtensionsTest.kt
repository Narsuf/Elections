package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Result
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateElection
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateElections
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateResult
import com.jorgedguezm.elections.data.utils.ElectionRandomGenerator.Companion.generateResults
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ElectionListExtensionsTest {

    @Test
    fun sortElections() {
        val elections = generateElections()
        val sortedElections = elections.sortByDateAndFormat()
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
        val election = generateElection()
            .copy(results = generateResults().addDuplicatedParties())
            .sortResultsByElectsAndVotes()
        var lastResult: Result? = null

        election.results.forEach {
            val currentElects = it.elects
            val currentVotes = it.votes
            val lastElects = lastResult?.elects ?: Int.MAX_VALUE
            val lastVotes = lastResult?.votes ?: Int.MAX_VALUE

            assertTrue(currentElects <= lastElects)
            if (currentElects == lastElects) assertTrue(currentVotes <= lastVotes)

            lastResult = it
        }
    }

    /**
     *  Adds duplicated parties with same elects but different votes.
     */
    private fun List<Result>.addDuplicatedParties(): List<Result> {
        val extraResults = mutableListOf<Result>()
        extraResults.addAll(this)

        val result = generateResult().copy(votes = Int.MAX_VALUE)
        val result2 = result.copy(votes = Int.MIN_VALUE)

        extraResults.add(result)
        extraResults.add(result2)

        return extraResults
    }
}
