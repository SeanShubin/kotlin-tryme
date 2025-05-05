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

  fun <T, U> mapAddToList(map:Map<T, List<U>>, pair: Pair<T, U>):Map<T, List<U>>{
    val (key, value) = pair
    val oldList = map[key] ?: emptyList()
    val newList = oldList + value
    return map + (key to newList)
  }

  fun <T,U> Map<T, List<U>>.addToList(pair: Pair<T, U>):Map<T, List<U>> = mapAddToList<T,U>(this, pair)
}
