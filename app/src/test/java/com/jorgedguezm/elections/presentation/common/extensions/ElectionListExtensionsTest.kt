package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.utils.ElectionGenerator.Companion.generateElections
import junit.framework.TestCase
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
            TestCase.assertTrue(currentDate <= lastDate)
            lastElection = it
        }
    }
}
