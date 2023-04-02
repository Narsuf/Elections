package com.n27.core.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class IntExtensionsTest {

    @Test
    fun toStringId() {
        assertEquals("01", 1.toStringId())
        assertEquals("10", 10.toStringId())
    }
}
