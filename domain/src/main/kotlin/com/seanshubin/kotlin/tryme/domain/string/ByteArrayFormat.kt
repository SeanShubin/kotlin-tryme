package com.seanshubin.kotlin.tryme.domain.string

interface ByteArrayFormat {
    fun encodeCompact(bytes: ByteArray): String
    fun encodePretty(bytes: ByteArray): String
    fun decode(s: String): ByteArray
}
