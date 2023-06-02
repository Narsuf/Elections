package com.n27.core.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class ListExtensionsTest {

    @Test
    fun toStringId() {
        val list1 = listOf(1, 2, 3, 4)
        val list2 = listOf(1, 5, 3, 6)

        val changedItems = list1.compare(list2)

        assertEquals(1, changedItems[0])
        assertEquals(3, changedItems[1])
    }
}