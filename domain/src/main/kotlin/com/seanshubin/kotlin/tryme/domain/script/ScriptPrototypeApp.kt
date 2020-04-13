package com.seanshubin.kotlin.tryme.domain.script

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

object ScriptPrototypeApp {
  @JvmStatic
  fun main(args: Array<String>) {
    val charset: Charset = StandardCharsets.UTF_8
    val scriptPathName = "sample-data/script.txt"
    val files: FilesContract = FilesDelegate
    val scriptParser = ScriptParserImpl(files, charset)
    val scriptPath = Paths.get(scriptPathName)
    val api = ApiImpl(files)
    val commands = scriptParser.parse(scriptPath)
    val initialState = State.empty
    val commandExecutor = CommandExecutorImpl(api)
    val state = commands.fold(initialState, commandExecutor::executeCommand)
    println(state.commandsExecuted)
  }
}