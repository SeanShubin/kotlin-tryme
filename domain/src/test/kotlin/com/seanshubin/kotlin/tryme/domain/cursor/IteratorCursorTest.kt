package com.seanshubin.kotlin.tryme.domain.cursor

import kotlin.test.*

class IteratorCursorTest {
    @Test
    fun value() {
        val s = "abc"
        val cursorA = IteratorCursor.create(s)
        val cursorB = cursorA.next()
        val cursorC = cursorB.next()
        val cursorEnd = cursorC.next()
        assertEquals('a', cursorA.value)
        assertEquals('b', cursorB.value)
        assertEquals('c', cursorC.value)
        try {
            cursorEnd.value
            fail("Should have thrown exception")
        } catch (ex: Exception) {
            assertEquals(ex.message, "No value past end of iterator")
        }
    }

    @Test
    fun end() {
        val s = "abc"
        val cursorA = IteratorCursor.create(s)
        val cursorB = cursorA.next()
        val cursorC = cursorB.next()
        val cursorEnd = cursorC.next()
        assertFalse(cursorA.isEnd)
        assertFalse(cursorB.isEnd)
        assertFalse(cursorC.isEnd)
        assertTrue(cursorEnd.isEnd)
    }

    @Test
    fun pastEnd() {
        val s = "abc"
        val cursorA = IteratorCursor.create(s)
        val cursorB = cursorA.next()
        val cursorC = cursorB.next()
        val cursorEnd = cursorC.next()
        try {
            cursorEnd.next()
            fail("Should have thrown exception")
        } catch (ex: Exception) {
            assertEquals(ex.message, "No next() past end of iterator")
        }
    }

    @Test
    fun valueIs() {
        val s = "abc"
        val cursorB = IteratorCursor.create(s).next()
        assertFalse(cursorB.valueIs('a'))
        assertTrue(cursorB.valueIs('b'))
    }

    @Test
    fun valueIsPredicate() {
        val s = "abc"
        val cursorB = IteratorCursor.create(s).next()
        assertFalse(cursorB.valueIs { it == 'a' })
        assertTrue(cursorB.valueIs { it == 'b' })
    }

    @Test
    fun immutable() {
        val s = "abc"
        val cursorA = IteratorCursor.create(s)
        val cursorB1 = cursorA.next()
        val cursorB2 = cursorA.next()
        val cursorC1 = cursorB1.next()
        val cursorC2 = cursorB2.next()

        assertEquals('b', cursorB1.value)
        assertEquals('b', cursorB2.value)
        assertEquals('c', cursorC1.value)
        assertEquals('c', cursorC2.value)

    }

    @Test
    fun between() {
        // given
        val s = "abcde"
        val cursorA = IteratorCursor.create(s)
        val cursorB = cursorA.next()
        val cursorC = cursorB.next()
        val cursorD = cursorC.next()
        val cursorE = cursorD.next()
        val expected = listOf('b', 'c', 'd')

        // when
        val actual = cursorB.between(cursorE)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun betweenNeverEncountersCursor() {
        // given
        val s = "abcde"
        val cursorA = IteratorCursor.create(s)
        val cursorB = cursorA.next()
        val cursorC = cursorB.next()
        val cursorD = cursorC.next()
        val cursorE = cursorD.next()

        // when
        try {
            cursorE.between(cursorB)
            fail("Should have thrown exception")
        } catch (ex: Exception) {
            assertEquals(ex.message, "Unable to navigate from begin to end cursor")
        }
    }

    @Test
    fun sameReference() {
        val s = "abc"
        val cursorA = IteratorCursor.create(s)
        val cursorB1 = cursorA.next()
        val cursorB2 = cursorA.next()
        val cursorC1 = cursorB1.next()
        val cursorC2 = cursorB2.next()

        assertSame(cursorB1, cursorB2)
        assertSame(cursorC1, cursorC2)
    }

}
