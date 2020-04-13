package com.seanshubin.kotlin.tryme.domain.shell

class LineConsumer(private val event: (String) -> Unit) : (String) -> Unit {
  val lines = mutableListOf<String>()
  override fun invoke(line: String) {
    event(line)
    lines.add(line)
  }
}