package com.seanshubin.kotlin.tryme.domain.tree

interface Tree<T> {
  val value: T
  fun toList(): List<T>
}
