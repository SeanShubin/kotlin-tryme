package com.seanshubin.kotlin.tryme.domain.script

data class State(val commandsExecuted: Int) {
  fun increment(): State = State(commandsExecuted + 1)

  companion object {
    val empty = State(0)
  }
}
