package com.seanshubin.kotlin.tryme.domain.string

import java.util.*

object ByteArrayFormatBase64 : ByteArrayFormat {
    override fun encodeCompact(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes).removeTrailing('=')
    }

    override fun encodePretty(bytes: ByteArray): String {
        val compact = encodeCompact(bytes)
        val lines = compact.chunked(76)
        return lines.joinToString("\n")
    }

    override fun decode(s: String): ByteArray {
        val compact = s.trim().split(whitespace).joinToString("")
        return Base64.getDecoder().decode(compact)
    }

    private val whitespace = Regex("""\s+""")
    private fun String.removeTrailing(c: Char): String =
        if (endsWith(c)) substring(0, length - 1).removeTrailing(c)
        else this
}
