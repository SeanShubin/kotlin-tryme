package com.seanshubin.kotlin.tryme.domain.dynamic

import javax.xml.crypto.Data
import kotlin.test.Test
import kotlin.test.assertEquals


class DataStructureAnalyzerTest {
    @Test
    fun empty() {
        val expected = mapOf<String, Map<String, Int>>()
        val actual = DataStructureAnalyzer.empty.map
        assertEquals(expected, actual)
    }

    @Test
    fun nullValue() {
        val input = null
        val expected = mapOf(
            "" to mapOf("NULL" to 1)
        )
        val analyzer = DataStructureAnalyzer.empty.addObject(input)
        val actual = analyzer.map
        assertEquals(expected, actual)
    }

    @Test
    fun mapArrayValue() {
        val input = """{ "a": { "b": [ 1, 2, 3 ] } }"""
        val expected = mapOf(
            "a.b.[" to mapOf("INT" to 3)
        )
        val analyzer = DataStructureAnalyzer.empty.addJson(input)
        val actual = analyzer.map
        assertEquals(expected, actual)
    }

    @Test
    fun primitives() {
        val input = """
            {
              "a": 1,
              "b": "c",
              "d": true,
              "e": false,
              "f": null,
              "g": 3.14
            }
        """.trimIndent()
        val expected = mapOf(
            "a" to mapOf("INT" to 1),
            "b" to mapOf("STRING" to 1),
            "d" to mapOf("BOOLEAN" to 1),
            "e" to mapOf("BOOLEAN" to 1),
            "f" to mapOf("NULL" to 1),
            "g" to mapOf("DOUBLE" to 1)
        )
        val analyzer = DataStructureAnalyzer.empty.addJson(input)
        val actual = analyzer.map
        assertEquals(expected, actual)
    }

    @Test
    fun nestedInString() {
        val input = """
            {
              "a": "1",
              "b": "c",
              "d": "true",
              "e": "false",
              "f": "null",
              "g": "3.14",
              "h": "[1,2,3]",
              "i": "{\"a\":1}"                            
            }
        """.trimIndent()
        val expected = mapOf(
            "a" to mapOf("INT_AS_STRING" to 1),
            "b" to mapOf("STRING" to 1),
            "d" to mapOf("BOOLEAN_AS_STRING" to 1),
            "e" to mapOf("BOOLEAN_AS_STRING" to 1),
            "f" to mapOf("NULL_AS_STRING" to 1),
            "g" to mapOf("DOUBLE_AS_STRING" to 1),
            "h" to mapOf("ARRAY_AS_STRING" to 1),
            "i" to mapOf("OBJECT_AS_STRING" to 1)
        )
        val analyzer = DataStructureAnalyzer.empty.addJson(input)
        val actual = analyzer.map
        assertEquals(expected, actual)
    }
}