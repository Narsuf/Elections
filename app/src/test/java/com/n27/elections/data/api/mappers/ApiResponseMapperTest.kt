package com.n27.elections.data.api.mappers

import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.data.api.models.ApiResponse
import com.n27.test.generators.getElections
import com.n27.test.generators.getElectionsWithResultsAndParty
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ApiResponseMapperTest {

    @Test
    fun `should return expected elections`() {
        val actual = ApiResponse(getElections()).toElections()
        val expected = getElections()
            .map { it.sortResultsByElectsAndVotes() }
            .sortByDateAndFormat()

        assertEquals(expected, actual)
    }
}
