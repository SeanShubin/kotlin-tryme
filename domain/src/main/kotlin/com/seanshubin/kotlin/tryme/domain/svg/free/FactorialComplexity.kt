package com.seanshubin.kotlin.tryme.domain.svg.free

import java.nio.file.Files
import java.nio.file.Paths

object FactorialComplexity {
    const val fullCircle = 2 * Math.PI

    fun makePolygonPointIndexSides(index: Int, sides: Int): DoublePoint {
        val radius = 100.0
        val angle = fullCircle / sides * index
        return DoublePoint.createRadiusAngle(radius, angle)
    }

    fun pointsToLines(points: List<DoublePoint>): List<Line> {
        val lines = mutableListOf<Line>()
        for (i in 0 until points.size) {
            for (j in i until points.size) {
                if (i != j) {
                    lines.add(Line(points[i], points[j]))
                }
            }
        }
        return lines
    }

    fun s(quantity: Int): String = if (quantity == 1) "" else "s"

    fun svgPolygon(sides: Int): List<String> {
        val header =
            listOf("""<span>""", """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="210" height="210">""")
        val footer = listOf("""</svg>""", """</span>""")
        val makePolygonPoint = { index: Int -> makePolygonPointIndexSides(index, sides) }
        val points = (0 until sides).map(makePolygonPoint)
        val lines = pointsToLines(points)
        val pointsAndLines: List<Entity> = points + lines
        val entitySvgLines = pointsAndLines.map(Entity::toScreen).map(Entity::toSvg)
        val size = lines.size
        val descriptionLines = listOf(
            """<div>$sides component${s(sides)} results in $size possible coupling${s(size)}</div>""",
            """<hr style="border-style=solid; border-width=1px"></hr>"""
        )
        val svgLines = header + entitySvgLines + descriptionLines + footer
        return svgLines
    }

    fun svgLines(): List<String> {
        return (1..26).flatMap(::svgPolygon)
    }

    fun htmlLines(): List<String> {
        val header = listOf(
            """<!DOCTYPE html>""",
            """<html lang="en">""",
            """<head>""",
            """<meta charset="UTF-8">""",
            """<title>Factorial Complexity</title>""",
            """</head>""",
            """<body>"""
        )
        val footer = listOf(
            """</body>""",
            """</html>"""
        )
        val svgLines = svgLines()
        return header + svgLines + footer
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val path = Paths.get("factorial-complexity.html")
        val lines = htmlLines()
        Files.write(path, lines)
        lines.forEach(::println)
    }
}
