package com.seanshubin.kotlin.tryme.domain.svg.grid

data class Rectangle(val top: Int,
                     val left: Int,
                     val bottom: Int,
                     val right: Int) : Shape {
  override fun toString(): String = this.javaClass.simpleName
  override fun toSvg(): List<String> {
    val topLeft = Point(left, top)
    val topRight = Point(right, top)
    val bottomLeft = Point(left, bottom)
    val bottomRight = Point(right, bottom)
    val top = Line(topLeft, topRight)
    val bottom = Line(bottomLeft, bottomRight)
    val left = Line(topLeft, bottomLeft)
    val right = Line(topRight, bottomRight)
    val parts = listOf(top, bottom, left, right)
    return parts.flatMap { it.toSvg() }
  }
}
