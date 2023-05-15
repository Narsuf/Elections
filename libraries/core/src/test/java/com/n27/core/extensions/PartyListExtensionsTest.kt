package com.n27.core.extensions

import com.n27.test.generators.getParties
import com.n27.test.generators.getPartiesRaw
import org.junit.Assert.assertEquals
import org.junit.Test

class PartyListExtensionsTest {

    @Test
    fun lowercaseNames() {
        val parties = getParties().lowercaseNames()

        assertEquals(parties[0].name, "pp")
    }
}
