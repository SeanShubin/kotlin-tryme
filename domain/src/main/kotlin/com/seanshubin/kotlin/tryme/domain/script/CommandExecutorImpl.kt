package com.seanshubin.kotlin.tryme.domain.script

class CommandExecutorImpl(
    private val api: Api
) : CommandExecutor {
  override fun executeCommand(state: State, command: Command): State {
    return command.execute(state, api)
  }
}
