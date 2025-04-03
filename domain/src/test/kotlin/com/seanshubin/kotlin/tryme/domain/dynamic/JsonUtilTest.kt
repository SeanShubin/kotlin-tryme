package com.seanshubin.kotlin.tryme.domain.dynamic

import kotlin.test.assertEquals
import kotlin.test.Test

class JsonUtilTest {
    @Test
    fun parseCsvLine() {
        val csvLine = """aaa,""${'"'}bbb""${'"'},"{""a"":""{\n  \""b\"":\""c\""\n}\n""}","aaa""bbb","a,b,c""""
        val expected = listOf(
            "aaa",
            "\"bbb\"",
            """{"a":"{\n  \"b\":\"c\"\n}\n"}""",
            "aaa\"bbb",
            "a,b,c"
        )
        val actual = JsonUtil.parseCsvLine(csvLine)
        assertEquals(expected, actual)
    }

    @Test
    fun coerceStrings() {
        val o = """
            {
              "a": "{\n  \"b\": \"{\\n  \\\"c\\\": \\\"{\\\\n  \\\\\\\"d\\\\\\\": \\\\\\\"123\\\\\\\"\\\\n}\\\\n\\\"\\n}\\n\"\n}\n",
              "b": "true",
              "c": "false",
              "d": "null",
              "e": "123",
              "f": "[1,2,3]"
            }
        """.trimIndent()
        val expected = mapOf(
            "a" to mapOf("b" to mapOf("c" to mapOf("d" to 123))),
            "b" to true,
            "c" to false,
            "d" to null,
            "e" to 123,
            "f" to listOf(1, 2, 3)
        )
        val actual = JsonUtil.coerceStrings(o)
        assertEquals(expected, actual)
    }
}
