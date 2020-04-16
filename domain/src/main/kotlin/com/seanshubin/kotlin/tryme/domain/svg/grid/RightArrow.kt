package com.seanshubin.kotlin.tryme.domain.svg.grid

data class RightArrow(val source: Point, val length: Int) : Shape {
  override fun toString(): String = this.javaClass.simpleName
  override fun toSvg(): List<String> {
    val destination = source.add(length, 0)
    val body = Line(source, destination)
    val leftFin = Line(destination, destination.add(-1, 1))
    val rightFin = Line(destination, destination.add(-1, -1))
    val parts = listOf(body, leftFin, rightFin)
    return parts.flatMap { it.toSvg() }
  }
}
