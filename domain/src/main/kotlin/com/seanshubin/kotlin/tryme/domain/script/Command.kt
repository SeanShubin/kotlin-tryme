package com.seanshubin.kotlin.tryme.domain.script

import java.nio.file.Path

sealed class Command {
  abstract fun execute(state: State, api: Api): State
  data class CreateDir(val path: Path) : Command() {
    override fun execute(state: State, api: Api): State {
      return api.createDir(state, path)
    }
  }

  data class CreateFile(val path: Path) : Command() {
    override fun execute(state: State, api: Api): State {
      return api.createFile(state, path)
    }
  }

  data class WriteLine(val path: Path, val line: String) : Command() {
    override fun execute(state: State, api: Api): State {
      return api.createDir(state, path)
    }
  }
}
