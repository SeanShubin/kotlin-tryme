package com.seanshubin.kotlin.tryme.domain.dependency

import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
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
        actual.toLines(emptyList()).forEach(::println)
    }

    @Test
    fun contextTruncate(){
        val input = listOf(
            listOf("a", "b") to listOf("c", "d")
        )
        val expected = listOf("a -> c")
        val actual = Graph.create(input).perspective(listOf()).toLines(emptyList())
        assertEquals(expected, actual)
    }

    @Test
    fun contextNarrow(){
        val input = listOf(
            listOf("a", "b") to listOf("a", "c")
        )
        val expected = listOf("b -> c")
        val actual = Graph.create(input).perspective(listOf("a")).toLines(emptyList())
        assertEquals(expected, actual)
    }

    @Test
    fun sample(){
        val input = listOf(
            listOf("a", "b") to listOf("c", "d"),
            listOf("c", "d") to listOf("e", "f"),
            listOf("e", "f") to listOf("g", "h"),
            listOf("g", "h") to listOf("c", "d"),
            listOf("g", "h") to listOf("i", "j"),
            listOf("e", "f", "k") to listOf("e", "f", "l"),
            listOf("e", "f", "l") to listOf("e", "f", "m"),
            listOf("e", "f", "m") to listOf("e", "f", "n"),
            listOf("e", "f", "n") to listOf("e", "f", "l"),
            listOf("e", "f", "n") to listOf("e", "f", "o"),
            listOf("c", "d") to listOf("e", "f", "l"),
            listOf("e", "f", "n") to listOf("g", "h")
        )
        val actual = Graph.create(input)
        val perspectives = actual.generatePerspectives(emptyList())
        val baseDir = Paths.get("..", "generated", "report", "dependency")
        Files.createDirectories(baseDir)
        perspectives.forEach{ (context, graph) ->
            val nameParts = listOf("dependency") + context
            val name = nameParts.joinToString("-") + ".txt"
            val file = baseDir.resolve(name)
            val lines =graph.toLines(context)
            Files.write(file, lines)
        }
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