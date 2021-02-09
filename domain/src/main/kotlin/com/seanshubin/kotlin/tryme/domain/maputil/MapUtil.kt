package com.seanshubin.kotlin.tryme.domain.maputil

object MapUtil {
  fun <A, B> Map<A, Set<B>>.invert(): Map<B, Set<A>> {
    val result = mutableMapOf<B, MutableSet<A>>()
    forEach { key, values ->
      values.forEach { value ->
        if (!result.containsKey(value)) {
          result[value] = mutableSetOf<A>()
        }
        result.getValue(value) += key
      }
    }
    return result
  }
}
