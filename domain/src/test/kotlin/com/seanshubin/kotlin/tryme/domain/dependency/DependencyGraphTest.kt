package com.seanshubin.kotlin.tryme.domain.dependency

import org.junit.Test
import kotlin.test.assertEquals

class DependencyGraphTest {
    @Test
    fun typical() {
        // given
        val input = listOf(
            "a" to "b",
            "b" to "c",
            "c" to "d",
            "d" to "e",
            "e" to "f",
            "f" to "g",
            "g" to "h",
            "d" to "b",
            "g" to "e"
        ).stringToPath()

        val actual = Graph.create(input)
        assertEquals(listOf("a"), actual.entryPoints.map{it.simpleName})
        assertEquals(7, actual.moduleDepth(ModulePath("a")))
        assertEquals(1, actual.moduleBreadth(ModulePath("a")))
        assertEquals(7, actual.moduleTransitive(ModulePath("a")))
        actual.toLines().forEach(::println)
    }

    @Test
    fun context(){
        val input = listOf(
            listOf("a", "b") to listOf("c", "d")
        )
        val actual = Graph.create(input)
        actual.toLines().forEach(::println)

    }

    fun String.stringToPath():List<String> = listOf(this)

    fun Pair<String, String>.stringToPath():Pair<List<String>, List<String>> = Pair(
        first.stringToPath(),
        second.stringToPath()
    )

    fun Set<Pair<String, String>>.stringToPath():Set<Pair<List<String>, List<String>>> =
        map{it.stringToPath()}.toSet()

    fun List<Pair<String, String>>.stringToPath():List<Pair<List<String>, List<String>>> =
        map{it.stringToPath()}
}