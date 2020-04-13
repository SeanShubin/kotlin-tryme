package com.seanshubin.kotlin.tryme.domain.rgr

import org.junit.Test
import kotlin.test.assertEquals

class AbsoluteValueTest {
  @Test
  fun positive() {
    // given
    val input = 4
    val expected = 4

    // when
    val actual = absoluteValue(input)

    // then
    assertEquals(expected, actual)
  }

  @Test
  fun negative() {
    // given
    val input = -5
    val expected = 5

    // when
    val actual = absoluteValue(input)

    // then
    assertEquals(expected, actual)
  }

  fun absoluteValue(input: Int): Int =
      if (input < 0) -input else input
}
