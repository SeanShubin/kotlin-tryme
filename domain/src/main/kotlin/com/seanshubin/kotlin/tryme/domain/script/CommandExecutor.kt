package com.seanshubin.kotlin.tryme.domain.script

interface CommandExecutor {
  fun executeCommand(state: State, command: Command): State
}
