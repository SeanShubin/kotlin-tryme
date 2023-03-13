package com.seanshubin.kotlin.tryme.domain.compare

import kotlin.test.Test
import kotlin.test.assertEquals

class ListComparatorTest {
    @Test
    fun compareStrings() {
        val comparator = ListComparator<String>()
        assertEquals(0, comparator.compare(null, null))
        assertEquals(1, comparator.compare(listOf(), null))
        assertEquals(-1, comparator.compare(null, listOf()))
        assertEquals(0, comparator.compare(listOf(), listOf()))
        assertEquals(1, comparator.compare(listOf("a"), listOf()))
        assertEquals(-1, comparator.compare(listOf(), listOf("a")))
        assertEquals(0, comparator.compare(listOf("a"), listOf("a")))
        assertEquals(-1, comparator.compare(listOf("a"), listOf("b")))
        assertEquals(1, comparator.compare(listOf("b"), listOf("a")))
        assertEquals(-1, comparator.compare(listOf("a"), listOf("a", "b")))
        assertEquals(1, comparator.compare(listOf("a", "b"), listOf("a")))
    }

    @Test
    fun compareNumbers() {
        val comparator = ListComparator<Int>()
        assertEquals(0, comparator.compare(null, null))
        assertEquals(1, comparator.compare(listOf(), null))
        assertEquals(-1, comparator.compare(null, listOf()))
        assertEquals(0, comparator.compare(listOf(), listOf()))
        assertEquals(1, comparator.compare(listOf(1), listOf()))
        assertEquals(-1, comparator.compare(listOf(), listOf(1)))
        assertEquals(0, comparator.compare(listOf(1), listOf(1)))
        assertEquals(-1, comparator.compare(listOf(1), listOf(2)))
        assertEquals(1, comparator.compare(listOf(2), listOf(1)))
        assertEquals(-1, comparator.compare(listOf(1), listOf(1, 2)))
        assertEquals(1, comparator.compare(listOf(1, 2), listOf(1)))
    }
}
