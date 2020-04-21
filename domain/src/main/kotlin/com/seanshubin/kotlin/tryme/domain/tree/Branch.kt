package com.seanshubin.kotlin.tryme.domain.tree

data class Branch<T>(override val value: T, val children: List<Tree<T>>) : Tree<T> {
  override fun toList(): List<T> =
      listOf(value) + children.flatMap { it.toList() }
}
