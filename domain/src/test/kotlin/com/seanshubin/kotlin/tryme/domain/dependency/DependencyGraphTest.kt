package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.process.ProcessInput
import com.seanshubin.kotlin.tryme.domain.process.ProcessRunner
import com.seanshubin.kotlin.tryme.domain.process.SystemProcessRunner
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

        val actual = Graph.create(input, emptyList())
        assertEquals(listOf("a"), actual.entryPoints.map { it.simpleName })
        assertEquals(7, actual.moduleDepth(ModulePath("a")))
        assertEquals(1, actual.moduleBreadth(ModulePath("a")))
        assertEquals(7, actual.moduleTransitive(ModulePath("a")))
        assertEquals(0, actual.moduleDepth(ModulePath("h")))
    }

    @Test
    fun contextTruncate() {
        val input = listOf(
            listOf("a", "b") to listOf("c", "d")
        )
        val expected = listOf(
            "digraph detangled {",
            "  a -> c",
            "}"
        )
        val actual = Graph.create(input, emptyList()).perspective(listOf()).toLines(nullMakeLink)
        assertEquals(expected, actual)
    }

    @Test
    fun contextNarrow() {
        val input = listOf(
            listOf("a", "b") to listOf("a", "c")
        )
        val expected = listOf(
            "digraph detangled {",
            "  b -> c",
            "}"
        )
        val actual = Graph.create(input, emptyList()).perspective(listOf("a")).toLines(nullMakeLink)
        assertEquals(expected, actual)
    }

    @Test
    fun sample() {
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
        val processRunner: ProcessRunner = SystemProcessRunner()
        val actual = Graph.create(input, emptyList())
        val perspectives = actual.generatePerspectives(emptyList())

        val baseDir = Paths.get("..", "generated", "report", "dependency")
        Files.createDirectories(baseDir)
        val availablePerspectives = perspectives.map { it.first }
        perspectives.forEach { (context, graph) ->
            val nameParts = listOf("dependency") + context
            fun makeLink(modulePath: ModulePath): String? {
                val destination = context + modulePath.pathParts
                return if (availablePerspectives.contains(destination)) {
                    val linkParts = listOf("dependency") + context + modulePath.pathParts
                    val linkName = linkParts.joinToString("-") + ".svg"
                    "${modulePath.simpleName} [URL=\"$linkName\" fontcolor=Blue]"
                } else {
                    null
                }
            }

            val baseName = nameParts.joinToString("-")
            val dotName = baseName + ".txt"
            val svgName = baseName + ".svg"
            val file = baseDir.resolve(dotName)
            val lines = graph.toLines(::makeLink)
            Files.write(file, lines)
            val command = listOf(
                "dot",
                "-Tsvg",
                "-o$svgName",
                dotName
            )
            val processInput = ProcessInput(command, baseDir)
            val processOutput = processRunner.run(processInput)
            if(processOutput.exitCode != 0){
                throw RuntimeException("$processInput $processOutput")
            }
        }
    }

    /*
      e [URL="dependency-e.svg" fontcolor=Blue]

     */
    fun String.stringToPath(): List<String> = listOf(this)

    fun Pair<String, String>.stringToPath(): Pair<List<String>, List<String>> = Pair(
        first.stringToPath(),
        second.stringToPath()
    )

    fun Set<Pair<String, String>>.stringToPath(): Set<Pair<List<String>, List<String>>> =
        map { it.stringToPath() }.toSet()

    fun List<Pair<String, String>>.stringToPath(): List<Pair<List<String>, List<String>>> =
        map { it.stringToPath() }

    val nullMakeLink: (ModulePath) -> String? = { null }
}