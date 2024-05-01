package com.seanshubin.kotlin.tryme.domain.huffman

import com.seanshubin.kotlin.tryme.domain.base.BaseConverter
import com.seanshubin.kotlin.tryme.domain.huffman.HuffmanNode.Companion.buildHuffmanTree

class HuffmanCodeTable(private val node:HuffmanNode) {
    fun encode(target: String): String {
        val binary = target.map{ char -> node.encode(char.toString())}.joinToString("")
        val base64 = binaryToBase64(binary)
//        println("encode.binary (${binary.length}) = $binary")
//        println("encode.base64 (${base64.length}) = $base64")
        return base64
    }

    fun decode(base64: String): String {
        val binary = base64ToBinary(base64)
//        println("decode.base64 (${base64.length}) = $base64")
//        println("decode.binary (${binary.length}) = $binary")
        return node.decode(binary)
    }

    companion object {
        val base64ToBinary = BaseConverter(BaseConverter.base64, BaseConverter.binary)
        val binaryToBase64 = BaseConverter(BaseConverter.binary, BaseConverter.base64)

        fun binaryToBase64(binary:String):String {
            val leadingZeroes = countLeadingZeroes(binary)
            val firstChar = BaseConverter.base64[leadingZeroes]
            val remainingChars = binaryToBase64.convert(binary)
            return firstChar + remainingChars
        }

        fun countLeadingZeroes(binary:String):Int {
            return binary.takeWhile { it == '0' }.length
        }

        fun base64ToBinary(base64:String):String{
            val leadingZeroes = BaseConverter.base64.indexOf(base64[0])
            val binary = base64ToBinary.convert(base64.substring(1))
            return "0".repeat(leadingZeroes) + binary
        }

        fun buildCodeTable(lines: List<String>): HuffmanCodeTable {
            val frequencies = lines.map(::histogram).merge()
            val node = frequencies.frequenciesToHuffmanNode()
            return HuffmanCodeTable(node)
        }

        fun histogram(s: String): Map<String, Int> {
            return s.map { c -> mapOf(c.toString() to 1) }.merge()
        }

        fun Map<String, Int>.frequenciesToHuffmanNode():HuffmanNode =
            map{ (value, frequency) ->
                HuffmanNode.Companion.Leaf(frequency, value)
            }.buildHuffmanTree()

        fun List<Map<String, Int>>.merge():Map<String, Int> {
            val emptyAccumulator = emptyMap<String, Int>()
            val histogram = fold(emptyAccumulator, ::mergeHistograms)
            return histogram
        }

        fun mergeHistograms(first:Map<String, Int>, second:Map<String, Int>):Map<String, Int> {
            val allKeys = first.keys + second.keys
            val combined = allKeys.associateWith { key ->
                (first[key] ?: 0) + (second[key] ?: 0)
            }
            return combined
        }
    }
}