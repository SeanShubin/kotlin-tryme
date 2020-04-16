package com.seanshubin.kotlin.tryme.domain.svg.free

data class Line(val p1: DoublePoint, val p2: DoublePoint) : Entity {
  override fun toScreen(): Line = Line(p1.toScreen(), p2.toScreen())

  override fun toSvg(): String {
    val x1 = p1.x
    val x2 = p2.x
    val y1 = p1.y
    val y2 = p2.y
    return """<line x1="$x1" y1="$y1" x2="$x2" y2="$y2" stroke="blue" stroke-width="4" />"""
  }

}
