package com.seanshubin.kotlin.tryme.domain.huffman

import kotlinx.coroutines.flow.merge
import kotlin.test.Test
import kotlin.test.assertEquals

class HuffmanTest {
    @Test
    fun execRoundTrip(){
        val phrase1 ="meet customer need"
        val phrase2 ="clearly express intent"
        val phrase3 ="no duplicate code"
        val phrase4 ="concise as possible"
        val phrases = listOf(
            phrase1,
            phrase2,
            phrase3,
            phrase4
        )
        val codeTable = HuffmanCodeTable.buildCodeTable(phrases)
        phrases.forEach{phrase ->
            assertRoundTrip(codeTable, phrase)
        }
        val encoded = codeTable.encode(phrase1)
        val decoded = codeTable.decode(encoded)
        assertEquals(phrase1, decoded)
    }

    private fun assertRoundTrip(codeTable:HuffmanCodeTable, text:String){
        println("text    (${text.length}) = $text")
        val encoded = codeTable.encode(text)
        println("encoded (${encoded.length}) = $encoded")
        val decoded = codeTable.decode(encoded)
        println("decoded (${decoded.length}) = $decoded")
        assertEquals(text, decoded)
    }

    @Test
    fun histogramTest(){
        val input = "aaabbc"
        val expected = mapOf("a" to 3, "b" to 2, "c" to 1)
        val actual = HuffmanCodeTable.histogram(input)
        assertEquals(expected, actual)
    }

}