package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers.normalizeJson
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class EmbeddedArrayFlattenTest {
    @Test
    fun flatten() {
        val input = """
            {
              "a": 1,
              "b": {
                "c": [
                  {"d": 2, "e": 3},
                  {"d": 4, "e": 5},
                  {"d": 6, "e": 7}
                ]
              }
            }
        """.trimIndent()
        val expected = listOf(
            """
            {
              "a": 1,
              "b": {
                "c": {
                  "d": 2,
                  "e": 3,
                  "c-reference": 123,
                  "c-index": 0
                }
              }
            }
            """.trimIndent(),
            """
            {
              "a": 1,
              "b": {
                "c": {
                  "d": 4,
                  "e": 5,
                  "c-reference": 123,
                  "c-index": 1
                }
              }
            }
            """.trimIndent(),
            """
            {
              "a": 1,
              "b": {
                "c": {
                  "d": 6,
                  "e": 7,
                  "c-reference": 123,
                  "c-index": 2
                }
              }
            }
            """.trimIndent()
        ).map{it.normalizeJson()}
        val referenceKey = "c-reference"
        val referenceValue = 123
        val indexKey = "c-index"
        val path = listOf("b", "c")
        val actual = EmbeddedArrayFlatten.flattenJson(input, path, referenceKey, referenceValue, indexKey).map{it.toJson()}.map{it.normalizeJson()}
        assertEquals(expected, actual)
    }
}
