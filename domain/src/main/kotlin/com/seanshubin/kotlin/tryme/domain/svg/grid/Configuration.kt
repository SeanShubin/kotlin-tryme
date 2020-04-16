package com.seanshubin.kotlin.tryme.domain.svg.grid

object Configuration {
  val coordinateSystem = object : CoordinateSystem {
    override fun adjustX(x: Int): Int {
      return x * 10 + 10
    }

    override fun adjustY(y: Int): Int {
      return y * 10 + 10
    }
  }
  val stroke: String = "blue"
  val strokeWidth: Int = 2
}
