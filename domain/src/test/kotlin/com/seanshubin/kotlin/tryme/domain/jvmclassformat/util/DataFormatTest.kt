package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import kotlin.test.Test

class DataFormatTest {
    @Test
    fun format(){
        val lines = listOf(
            DataFormat.formatDouble("double", 1.23456789),
            DataFormat.formatFloat("float", 1.2345678f),
            DataFormat.formatLong("long", 123456789L),
            DataFormat.formatInt("int", 123456789),
            DataFormat.formatShort("short", 12345.toShort()),
            DataFormat.formatByte("byte", 123.toByte()),
            DataFormat.formatChar("char", 'A'),
            DataFormat.formatBoolean("boolean", true),
            DataFormat.formatString("string", "Hello, World!"),
            DataFormat.formatBytes("bytes", "Hello, World!".toByteArray().toList())
        )
        lines.forEach(::println)
    }
}