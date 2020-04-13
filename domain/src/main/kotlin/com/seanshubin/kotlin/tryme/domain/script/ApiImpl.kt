package com.seanshubin.kotlin.tryme.domain.script

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.Path

class ApiImpl(private val files: FilesContract) : Api {
  override fun createDir(state: State, path: Path): State {
    return state.increment()
  }

  override fun createFile(state: State, path: Path): State {
    return state.increment()
  }

  override fun writeLine(state: State, path: Path, line: String): State {
    return state.increment()
  }
}
