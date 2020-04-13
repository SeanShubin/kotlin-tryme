package com.seanshubin.kotlin.tryme.domain.script

import java.nio.file.Path

interface ScriptParser {
  fun parse(path: Path): List<Command>
}
