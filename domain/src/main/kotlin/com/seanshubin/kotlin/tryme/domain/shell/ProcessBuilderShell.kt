package com.seanshubin.kotlin.tryme.domain.shell

import kotlinx.coroutines.runBlocking
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

class ProcessBuilderShell(private val runAsync: (() -> Unit) -> AsyncJob,
                          private val charset: Charset = StandardCharsets.UTF_8,
                          private val execEvent: (ShellRequest) -> Unit = {},
                          private val outputLineEvent: (String) -> Unit = {},
                          private val errorLineEvent: (String) -> Unit = {}) : Shell {

  override fun execute(shellRequest: ShellRequest): ShellResponse {
    val process = launchProcess(shellRequest)
    val outputLineConsumer = LineConsumer(outputLineEvent)
    val errorLineConsumer = LineConsumer(errorLineEvent)
    val outJob = runAsync {
      IoUtil.inputStreamToLineEvent(process.inputStream, charset, outputLineEvent)
    }
    val errJob = runAsync {
      IoUtil.inputStreamToLineEvent(process.errorStream, charset, errorLineEvent)
    }
    runBlocking {
      outJob.join()
      errJob.join()
    }
    val exitCode = process.waitFor()
    return ShellResponse(exitCode, outputLineConsumer.lines, errorLineConsumer.lines)
  }

  private fun launchProcess(shellRequest: ShellRequest): Process {
    execEvent(shellRequest)
    val (command, nullableDirectory, nullableEnvironment) = shellRequest
    val directory = nullableDirectory ?: Paths.get(".")
    val environment = nullableEnvironment ?: emptyMap()
    val processBuilder = ProcessBuilder()
    processBuilder.command(command)
    processBuilder.directory(directory.toFile())
    for (entry in environment) {
      processBuilder.environment().put(entry.key, entry.value)
    }
    val process = processBuilder.start()
    return process
  }
}
