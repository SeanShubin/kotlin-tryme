package com.seanshubin.kotlin.tryme.domain.coordinate

import kotlin.test.Test
import kotlin.test.assertEquals

class IntPointTest {
  @Test
  fun eighthTurnLeft0() {
    // given
    val point = IntPoint(0, 0)
    val expected = IntPoint(0, 0)

    // when
    val actual = point.rotateEighthLeft()

    // then
    assertEquals(expected, actual)
  }

  @Test
  fun eighthTurnLeft1() {
    val coordinates = listOf(
        1, 1,
        0, 1,
        -1, 1,
        -1, 0,
        -1, -1,
        0, -1,
        1, -1,
        1, 0,
        1, 1)
    rotateEighthLeftTester(coordinates)
  }

  @Test
  fun eighthTurnLeft2a() {
    val coordinates = listOf(
        2, 2,
        0, 2,
        -2, 2,
        -2, 0,
        -2, -2,
        0, -2,
        2, -2,
        2, 0,
        2, 2)
    rotateEighthLeftTester(coordinates)
  }

  @Test
  fun eighthTurnLeft2b() {
    val coordinates = listOf(
        1, 2,
        -1, 2,
        -2, 1,
        -2, -1,
        -1, -2,
        1, -2,
        2, -1,
        2, 1,
        1, 2)
    rotateEighthLeftTester(coordinates)
  }

  @Test
  fun eighthTurnLeft3a() {
    val coordinates = listOf(
        3, 3,
        0, 3,
        -3, 3,
        -3, 0,
        -3, -3,
        0, -3,
        3, -3,
        3, 0,
        3, 3)
    rotateEighthLeftTester(coordinates)
  }

  @Test
  fun eighthTurnLeft3b() {
    val coordinates = listOf(
        2, 3,
        -1, 3,
        -3, 2,
        -3, -1,
        -2, -3,
        1, -3,
        3, -2,
        3, 1,
        2, 3)
    rotateEighthLeftTester(coordinates)
  }

  @Test
  fun eighthTurnLeft3c() {
    val coordinates = listOf(
        1, 3,
        -2, 3,
        -3, 1,
        -3, -2,
        -1, -3,
        2, -3,
        3, -1,
        3, 2,
        1, 3)
    rotateEighthLeftTester(coordinates)
  }

  fun rotateEighthLeftTester(coordinates: List<Int>) {
    coordinates
        .chunked(2)
        .map(IntPoint.Companion::fromChunk)
        .windowed(2)
        .map(TestCase.Companion::fromChunk)
        .forEach(TestCase.run(IntPoint.Companion::rotateEighthLeft))
  }

  data class TestCase(val input: IntPoint, val expected: IntPoint) {
    fun run(f: (IntPoint) -> IntPoint) {
      val actual = f(input)
      assertEquals(expected, actual, input.toString())
    }

    companion object {
      fun fromChunk(chunk: List<IntPoint>): TestCase {
        if (chunk.size != 2) throw RuntimeException("chunk size must be exactly 2")
        val (from, to) = chunk
        return TestCase(from, to)
      }

      fun run(f: (IntPoint) -> IntPoint): (TestCase) -> Unit = { testCase ->
        testCase.run(f)
      }
    }
  }
}
