package com.seanshubin.kotlin.tryme.domain.maputil

import com.seanshubin.kotlin.tryme.domain.maputil.MapUtil.invert
import kotlin.test.Test
import kotlin.test.assertEquals

class NestedMapTest {
  @Test
  fun invert() {
    // given
    val input = mapOf(
        "a" to setOf(1, 2, 3),
        "b" to setOf(2, 3, 4),
        "c" to setOf(3, 4, 5)
    )
    val expected = mapOf(
        1 to setOf("a"),
        2 to setOf("a", "b"),
        3 to setOf("a", "b", "c"),
        4 to setOf("b", "c"),
        5 to setOf("c")
    )

    // when
    val actual = input.invert()

    // then
    assertEquals(expected, actual);
  }
}