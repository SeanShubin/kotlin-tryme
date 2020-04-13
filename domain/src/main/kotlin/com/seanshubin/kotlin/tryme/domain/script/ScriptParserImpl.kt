package com.seanshubin.kotlin.tryme.domain.script

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.io.IoUtil
import java.nio.charset.Charset
import java.nio.file.Path

class ScriptParserImpl(private val files: FilesContract,
                       private val charset: Charset) : ScriptParser {
  override fun parse(path: Path): List<Command> {
    val reader = files.newBufferedReader(path, charset)
    return reader.use {
      val text = IoUtil.readerToString(reader)
      val commandBlocks = text.trim().split(Regex("""\s*\n\n\s*"""))
      val commandSequences = commandBlocks.map {
        it.trim().split(Regex("""\s+"""))
      }
      val commands = commandSequences.map(CommandFactory::fromSequence)
      commands
    }
  }
}
