package com.n27.core.extensions

import com.n27.test.generators.getPartiesRaw
import org.junit.Assert.assertEquals
import org.junit.Test

class PartyRawListExtensionsTest {

    @Test
    fun lowercaseNames() {
        val parties = getPartiesRaw().lowercaseNames()

        assertEquals(parties[0].name, "pp")
    }
}
