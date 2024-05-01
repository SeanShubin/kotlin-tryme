package com.seanshubin.kotlin.tryme.domain.base64

import kotlin.test.Test
import kotlin.test.assertEquals

class Base64FormatterTest {
    val sampleBase64 =
            "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4" +
            "OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3Bx" +
            "cnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmq" +
            "q6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj" +
            "5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w=="
    val bytes =(0..255).map { it.toByte() }.toByteArray()

    @Test
    fun foo(){
        val byteArray = (0..31).map { it.toByte() }.toByteArray()
        println(Base64Formatter.format(byteArray))
    }

    @Test
    fun formatCompact() {
        val base64Formatter = Base64Formatter
        val input = bytes
        val actual = base64Formatter.format(input)
        val expected = sampleBase64
        assertEquals(expected, actual)
    }

    @Test
    fun parseCompact() {
        val input = sampleBase64
        val actual = Base64Formatter.parse(input)
        val expected = bytes
        assertEquals(expected.toList(), actual.toList())
    }
}
