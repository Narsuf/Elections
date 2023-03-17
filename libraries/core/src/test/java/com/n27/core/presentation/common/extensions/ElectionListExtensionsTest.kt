package com.n27.core.presentation.common.extensions

import com.n27.core.data.models.Election
import com.n27.core.data.models.Result
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateElection
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateElections
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateResult
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateResults
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