package com.seanshubin.kotlin.tryme.domain.svg.grid

data class Line(val from: Point, val to: Point) : Shape {
  override fun toSvg(): List<String> {
    val coordinateSystem = Configuration.coordinateSystem
    val x1 = coordinateSystem.adjustX(from.x)
    val y1 = coordinateSystem.adjustY(from.y)
    val x2 = coordinateSystem.adjustX(to.x)
    val y2 = coordinateSystem.adjustY(to.y)
    val stroke = Configuration.stroke
    val strokeWidth = Configuration.strokeWidth
    return listOf("""<line x1="$x1" y1="$y1" x2="$x2" y2="$y2" stroke="$stroke" stroke-width="$strokeWidth"/>""")
  }
}
