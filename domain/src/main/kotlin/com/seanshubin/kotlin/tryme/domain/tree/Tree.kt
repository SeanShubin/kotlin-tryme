package com.seanshubin.kotlin.tryme.domain.tree

data class Tree<T>(val value: T,
                   val children: List<Tree<T>> = emptyList()) {
  fun <U> map(f: (T) -> U): Tree<U> {
    val newValue = f(value)
    val newChildren = children.map { it.map(f) }
    return Tree(newValue, newChildren)
  }

  fun addChildPath(vararg parts: T): Tree<T> = addChildPath(parts.toList())

  fun addChildPath(parts: List<T>): Tree<T> {
    if (parts.isEmpty()) return this
    val part = parts[0]
    val existingIndex = children.indexOfFirst { it.value == part }
    return if (existingIndex == -1) {
      val remainingPath = parts.drop(1)
      val newChild = Tree(part, emptyList()).addChildPath(remainingPath)
      val newChildren = children + newChild
      Tree(value, newChildren)
    } else {
      val remainingPath = parts.drop(1)
      val existingChild = children[existingIndex]
      val newChild = existingChild.addChildPath(remainingPath)
      val newChildren = children.take(existingIndex) + newChild + children.drop(existingIndex + 1)
      Tree(value, newChildren)
    }
  }

  fun <U> mapWithCoordinates(f: (T, List<T>, List<Int>) -> U): Tree<U> = mapWithCoordinates(emptyList(), emptyList(), f)

  fun forEach(f: (Tree<T>) -> Unit) {
    f(this)
    children.forEach { it.forEach(f) }
  }

  fun forEachWithCoordinates(f: (Tree<T>, List<T>, List<Int>) -> Unit) =
          forEachWithCoordinates(emptyList(), emptyList(), f)

  fun toList(): List<T> = listOf(value) + children.flatMap { it.toList() }

  private fun <U> mapWithCoordinates(ancestors: List<T>, indices: List<Int>, f: (T, List<T>, List<Int>) -> U): Tree<U> {
    val newValue = f(value, ancestors, indices)
    val newAncestors = ancestors + value
    val newChildren = children.mapIndexed { childIndex, child ->
      val newIndices = indices + childIndex
      child.mapWithCoordinates(newAncestors, newIndices, f)
    }
    return Tree(newValue, newChildren)
  }

  private fun <U> forEachWithCoordinates(ancestors: List<T>, indices: List<Int>, f: (Tree<T>, List<T>, List<Int>) -> U) {
    f(this, ancestors, indices)
    val newAncestors = ancestors + value
    children.forEachIndexed { childIndex, child ->
      val newIndices = indices + childIndex
      child.forEachWithCoordinates(newAncestors, newIndices, f)
    }
  }
}
