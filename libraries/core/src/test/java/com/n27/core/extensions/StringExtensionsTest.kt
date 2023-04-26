package com.n27.core.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun removeAccents() {
        var actual = "Álava".removeAccents()
        assertEquals("Alava", actual)

        actual = "Òrrius".removeAccents()
        assertEquals("Orrius", actual)
    }
}