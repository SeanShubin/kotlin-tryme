package com.seanshubin.kotlin.tryme.domain.string

import java.nio.charset.StandardCharsets

object StringUtil {
    fun List<Byte>.toUtf8() = this.toByteArray().toString(StandardCharsets.UTF_8)
    fun String.escape(): String = this.flatMap(::escapeCharToIterable).joinToString("")
    private fun escapeCharToIterable(target: Char): Iterable<Char> = escapeCharToString(target).asIterable()
    private fun escapeCharToString(target: Char): String =
        when (target) {
            '\n' -> "\\n"
            '\b' -> "\\b"
            '\t' -> "\\t"
            '\r' -> "\\r"
            '\"' -> "\\\""
            '\'' -> "\\\'"
            '\\' -> "\\\\"
            else -> target.toString()
        }
    private val newlineRegex = Regex("\r\n|\r|\n")
    fun String.splitIntoLines():List<String> = split(newlineRegex)
}
