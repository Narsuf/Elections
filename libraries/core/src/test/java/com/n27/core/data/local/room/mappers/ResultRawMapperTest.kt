package com.n27.core.data.local.room.mappers

import com.n27.test.generators.getResult
import com.n27.test.generators.getResultRaw
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultRawMapperTest {

    @Test
    fun `should return expected resultRaw`() {
        val expected = getResultRaw()
        val actual = getResult().toResultRaw()

        assertEquals(expected, actual)
    }
}