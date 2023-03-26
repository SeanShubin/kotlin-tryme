package com.seanshubin.kotlin.tryme.domain.beam

sealed interface Tree<T> {
    val name: String
    fun toList(): List<T>
    fun toLines(): List<String>

    data class Leaf<T>(override val name: String, val value: T) : Tree<T> {
        override fun toList(): List<T> = listOf(value)
        override fun toLines(): List<String> = listOf("$name: $value")
    }

    data class Branch<T>(override val name: String, val list: List<Tree<T>>) : Tree<T> {
        override fun toList(): List<T> = list.flatMap { it.toList() }
        override fun toLines(): List<String> = listOf(name) + list.flatMap { it.toLines() }.map { "  $it" }
    }
}
