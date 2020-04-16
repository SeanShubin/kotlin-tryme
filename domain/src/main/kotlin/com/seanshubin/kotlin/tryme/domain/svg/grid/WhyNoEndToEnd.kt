package com.seanshubin.kotlin.tryme.domain.svg.grid

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object WhyNoEndToEnd {
  @JvmStatic
  fun main(args: Array<String>) {
    val layerCount = 4
    val pathCount = 5
    val shapes = mutableListOf<Shape>()
    (0 until pathCount).forEach { rowIndex ->
      (0 until layerCount).forEach { colIndex ->
        val top = rowIndex * 8
        val left = colIndex * 12
        val bottom = top + 4
        val right = left + 8
        shapes.add(Rectangle(top, left, bottom, right))
      }
    }
    (0 until pathCount).forEach { rowIndex ->
      (0 until layerCount - 1).forEach { colIndex ->
        val source = Point(colIndex * 12 + 8, rowIndex * 8 + 2)
        shapes.add(RightArrow(source, 4))
      }
    }
    val svgLines = wrapInSvg(500, 500, shapes.flatMap { it.toSvg() })
    val htmlText = wrapInHtml(svgLines)
    val path = Paths.get("test-paths.html")
    val charset = StandardCharsets.UTF_8
    Files.write(path, htmlText, charset)
  }

  fun wrapInSvg(width: Int, height: Int, middle: List<String>): List<String> =
      listOf("""<svg version="1.1" width="$width" height="$height">""") +
          middle +
          listOf("</svg>")

  fun wrapInHtml(middle: List<String>): List<String> {
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
    return header + middle + footer
  }
}
