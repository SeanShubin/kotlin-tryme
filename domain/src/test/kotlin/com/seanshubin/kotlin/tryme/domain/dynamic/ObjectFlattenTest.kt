package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers.normalizeJson
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectFlattenTest {
    @Test
    fun flatten() {
        val input = """
            {
              "a": 1,
              "b": {
                "c": {
                  "d": 2,
                  "e": 3
                }
              }
            }
        """.trimIndent()
        val expected = """
            {
              "a": 1,
              "b.c.d": 2,
              "b.c.e": 3
            }
        """.trimIndent().normalizeJson()
        val actual = ObjectFlatten.flattenJsonObject(input).normalizeJson()
        assertEquals(expected, actual)
    }
}
