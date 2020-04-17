package com.seanshubin.kotlin.tryme.domain.coordinate

import kotlin.math.abs
import kotlin.math.max

data class IntPoint(val x: Int, val y: Int) {
  fun rotateEighthLeft(): IntPoint {
    var result = this
    (0 until max).forEach { _ ->
      result = result.rotateOneLeft()
    }
    return result
  }

  private val max get() = max(abs(x), abs(y))
  private val min get() = -max
  private fun rotateOneLeft(): IntPoint {
    val newX =
        if (y == max && x != min) x - 1
        else if (y == min && x != max) x + 1
        else x
    val newY =
        if (x == max && y != max) y + 1
        else if (x == min && y != min) y - 1
        else y
    return IntPoint(newX, newY)
  }

  companion object {
    fun fromChunk(chunk: List<Int>): IntPoint {
      if (chunk.size != 2) throw RuntimeException("chunk size must be exactly 2")
      val (x, y) = chunk
      return IntPoint(x, y)
    }

    fun rotateEighthLeft(intPoint: IntPoint): IntPoint = intPoint.rotateEighthLeft()
  }
}
