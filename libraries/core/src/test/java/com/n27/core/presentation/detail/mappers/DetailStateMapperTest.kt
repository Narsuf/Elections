package com.n27.core.presentation.detail.mappers

import com.n27.test.generators.getDetailContent
import com.n27.test.generators.getElection
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailStateMapperTest {

    @Test
    fun `should return expected WithData`() {
        val expected = getDetailContent()
        val actual = getElection().toContent()

        assertEquals(expected, actual)
    }
}
