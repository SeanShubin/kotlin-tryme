package com.seanshubin.kotlin.tryme.domain.svg.grid

interface CoordinateSystem {
  fun adjustX(x: Int): Int
  fun adjustY(y: Int): Int
}
