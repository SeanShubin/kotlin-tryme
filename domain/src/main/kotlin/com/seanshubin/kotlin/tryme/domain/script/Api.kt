package com.seanshubin.kotlin.tryme.domain.script

import java.nio.file.Path

interface Api {
  fun createDir(state: State, path: Path): State
  fun createFile(state: State, path: Path): State
  fun writeLine(state: State, path: Path, line: String): State
}
