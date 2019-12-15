package com.seanshubin.kotlin.tryme.domain.compare

import kotlin.test.Test
import kotlin.test.assertEquals

class JvmNaturalSortTest {
    @Test
    fun compareNulls() {
        assertEquals(0, compare(null, null))
        assertEquals(-1, compare(null, ""))
        assertEquals(-1, compare(null, "a"))
        assertEquals(1, compare("", null))
        assertEquals(0, compare("", ""))
        assertEquals(-1, compare("", "a"))
        assertEquals(1, compare("a", null))
        assertEquals(1, compare("a", ""))
        assertEquals(0, compare("a", "a"))
    }

    private fun compare(left: String?, right: String?): Int =
        JvmNaturalSort.compare(left, right)
}

