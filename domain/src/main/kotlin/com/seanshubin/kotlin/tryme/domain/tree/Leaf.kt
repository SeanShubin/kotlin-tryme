package com.seanshubin.kotlin.tryme.domain.tree

data class Leaf<T>(override val value: T) : Tree<T> {
  override fun toList(): List<T> = listOf(value)
}
