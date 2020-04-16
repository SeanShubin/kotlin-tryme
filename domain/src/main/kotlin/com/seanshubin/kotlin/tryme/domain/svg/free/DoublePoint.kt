package com.seanshubin.kotlin.tryme.domain.svg.free

data class DoublePoint(val x: Double, val y: Double) : Entity {
  val radius: Double get() = Math.sqrt(x * x + y * y)
  val angle: Double get() = Math.atan(y / x)
  override fun toScreen(): DoublePoint = DoublePoint(x + 105, 105 - y)

  override fun toSvg(): String {
    return """<circle cx="$x" cy="$y" r="5" fill="blue"/>"""
  }

  companion object {
    fun createRadiusAngle(radius: Double, angle: Double): DoublePoint {
      val x = radius * Math.cos(angle)
      val y = radius * Math.sin(angle)
      return DoublePoint(x, y)
    }
  }
}
