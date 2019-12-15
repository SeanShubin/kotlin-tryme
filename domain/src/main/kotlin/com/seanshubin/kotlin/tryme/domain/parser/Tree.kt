package com.seanshubin.kotlin.tryme.domain.parser

interface Tree<T> {
    val name: String
    fun values(): List<T>
    fun toLines(depth: Int = 0): List<String>
    fun indent(s: String, depth: Int) = "  ".repeat(depth) + s
}
