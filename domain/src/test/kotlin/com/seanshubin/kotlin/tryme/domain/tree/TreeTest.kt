package com.seanshubin.kotlin.tryme.domain.tree

import kotlin.test.Test
import kotlin.test.assertEquals

class TreeTest {
    @Test
    fun createWithConstructor() {
        val b = Tree("b.txt")
        val c = Tree("c.txt")
        val g = Tree("g.txt")
        val a = Tree("a", listOf(b, c))
        val e = Tree("e")
        val f = Tree("f", listOf(g))
        val treeSample = Tree("tree-sample", listOf(a, e, f))
        val indentedDisplay: (String, List<String>, List<Int>) -> String = { value, _, indices ->
            "  ".repeat(indices.size) + value
        }
        val actual = treeSample.mapWithCoordinates(indentedDisplay).toList()
        val expected = listOf(
                "tree-sample",
                "  a",
                "    b.txt",
                "    c.txt",
                "  e",
                "  f",
                "    g.txt")
        assertEquals(expected, actual)
    }

    @Test
    fun createWithHelpers() {
        val treeSample = Tree("tree-sample")
                .addChildPath("a", "b.txt")
                .addChildPath("a", "c.txt")
                .addChildPath("e")
                .addChildPath("f", "g.txt")
        val indentedDisplay: (String, List<String>, List<Int>) -> String = { value, _, indices ->
            "  ".repeat(indices.size) + value
        }
        val actual = treeSample.mapWithCoordinates(indentedDisplay).toList()
        val expected = listOf(
                "tree-sample",
                "  a",
                "    b.txt",
                "    c.txt",
                "  e",
                "  f",
                "    g.txt")
        assertEquals(expected, actual)
    }

    @Test
    fun map() {
        val treeSample = Tree(2)
                .addChildPath(3, 4, 5)
                .addChildPath(3, 4, 6)
                .addChildPath(4, 6, 7)
                .addChildPath(4, 6, 8)
                .addChildPath(4, 6, 9)
                .addChildPath(5, 5, 7)
                .addChildPath(5, 6, 8)
                .addChildPath(5, 7, 9)
        val expected = Tree(4)
                .addChildPath(9, 16, 25)
                .addChildPath(9, 16, 36)
                .addChildPath(16, 36, 49)
                .addChildPath(16, 36, 64)
                .addChildPath(16, 36, 81)
                .addChildPath(25, 25, 49)
                .addChildPath(25, 36, 64)
                .addChildPath(25, 49, 81)
        val squared = { x: Int -> x * x }
        val actual = treeSample.map(squared)
        assertEquals(expected, actual)
    }
}
