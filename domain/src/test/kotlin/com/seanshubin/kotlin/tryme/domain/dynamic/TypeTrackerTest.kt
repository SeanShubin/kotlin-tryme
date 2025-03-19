package com.seanshubin.kotlin.tryme.domain.dynamic

import kotlin.test.Test
import kotlin.test.assertEquals

class TypeTrackerTest {
    @Test
    fun generateStructure() {
        val input = """
            {
              "a": 123,
              "b": "abc"
            }
        """.trimIndent()
        val expected = """
            data class Foo(
                val a: Int,
                val b: String
            )
        """.trimIndent()
        val typeTracker = TypeTracker.empty.addJson(input)
        val actual = typeTracker.dataClass("Foo")
        assertEquals(expected, actual)
    }
}
