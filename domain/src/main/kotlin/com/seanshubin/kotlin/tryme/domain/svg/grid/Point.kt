package com.seanshubin.kotlin.tryme.domain.svg.grid

data class Point(val x: Int, val y: Int) {
  fun add(dx: Int, dy: Int): Point = Point(x + dx, y + dy)
}
