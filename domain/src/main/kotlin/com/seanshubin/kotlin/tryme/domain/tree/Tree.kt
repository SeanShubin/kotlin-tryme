package com.seanshubin.kotlin.tryme.domain.tree

data class Tree<T>(val value: T,
                   val children: List<Tree<T>>) {
  fun <U> map(f: (T) -> U): Tree<U> {
    val newValue = f(value)
    val newChildren = children.map { it.map(f) }
    return Tree(newValue, newChildren)
  }

  fun <U> mapWithCoordinates(f: (T, List<T>, List<Int>) -> U): Tree<U> = mapWithCoordinates(emptyList(), emptyList(), f)

  private fun <U> mapWithCoordinates(ancestors: List<T>, indices: List<Int>, f: (T, List<T>, List<Int>) -> U): Tree<U> {
    val newValue = f(value, ancestors, indices)
    val newAncestors = ancestors + value
    val newChildren = children.mapIndexed { childIndex, child ->
      val newIndices = indices + childIndex
      child.mapWithCoordinates(newAncestors, newIndices, f)
    }
    return Tree(newValue, newChildren)
  }

  fun forEach(f: (Tree<T>) -> Unit) {
    f(this)
    children.forEach { it.forEach(f) }
  }

  fun forEachWithCoordinates(f: (Tree<T>, List<T>, List<Int>) -> Unit) =
      forEachWithCoordinates(emptyList(), emptyList(), f)

  private fun <U> forEachWithCoordinates(ancestors: List<T>, indices: List<Int>, f: (Tree<T>, List<T>, List<Int>) -> U) {
    f(this, ancestors, indices)
    val newAncestors = ancestors + value
    children.forEachIndexed { childIndex, child ->
      val newIndices = indices + childIndex
      child.forEachWithCoordinates(newAncestors, newIndices, f)
    }
  }
}
