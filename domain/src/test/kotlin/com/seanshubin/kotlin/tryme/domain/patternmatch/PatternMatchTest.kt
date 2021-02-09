package com.seanshubin.kotlin.tryme.domain.patternmatch

import kotlin.test.Test
import kotlin.test.assertEquals

class PatternMatchTest {
  @Test
  fun testPatternMatch() {
    data class Coordinate(val x: Int, val y: Int)

    val baseline = Coordinate(1, 2)
    val sameAsBaseline = Coordinate(1, 2)
    val xDifferent = Coordinate(3, 2)
    val yDifferent = Coordinate(1, 4)
    val yDifferentCopy = yDifferent.copy(x = 5)

    //notice that pattern matching does not 'fall through' as is the case with switch statements in Java
    fun describeCoordinate(coordinate: Coordinate): String {
      val (x, y) = coordinate
      return when {
        x == 1 && y == 2 -> "looks like baseline" //match the values exactly
        x == 1 -> "starts with 1" //match x to 1, don't care what y is
        y == 2 -> "ends with 2, x is $x" //match y to 2, recover the value for x
        else -> "another coordinate: values: x = $x, y = $y" //get the values for x and y
      }
    }
    assertEquals("looks like baseline", describeCoordinate(baseline))
    assertEquals("looks like baseline", describeCoordinate(sameAsBaseline))
    assertEquals("ends with 2, x is 3", describeCoordinate(xDifferent))
    assertEquals("starts with 1", describeCoordinate(yDifferent))
    assertEquals("another coordinate: values: x = 5, y = 4", describeCoordinate(yDifferentCopy))
  }
}
