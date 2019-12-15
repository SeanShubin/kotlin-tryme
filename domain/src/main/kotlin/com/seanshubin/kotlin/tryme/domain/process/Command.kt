package com.seanshubin.kotlin.tryme.domain.process

import java.nio.file.Path

interface Command {
    fun runInDirectory(processRunner: ProcessRunner, directory: Path): CommandResult
    fun expectZeroExitCode(input: ProcessInput, output: ProcessOutput) = CommandResult(output.exitCode == 0, input, output)
    fun expectNoOutputLines(input: ProcessInput, output: ProcessOutput) =
            CommandResult(output.exitCode == 0 && output.outputLines.isEmpty(), input, output)

    object GitFetch : Command {
        override fun runInDirectory(processRunner: ProcessRunner, directory: Path): CommandResult {
            val input = ProcessInput(listOf("git", "fetch"), directory)
            val output = processRunner.run(input)
            return expectZeroExitCode(input, output)
        }
    }

    object GitStatus : Command {
        override fun runInDirectory(processRunner: ProcessRunner, directory: Path): CommandResult {
            val input = ProcessInput(listOf("git", "status", "-s"), directory)
            val output = processRunner.run(input)
            return expectNoOutputLines(input, output)
        }
    }

    object GitLocalChanges : Command {
        override fun runInDirectory(processRunner: ProcessRunner, directory: Path): CommandResult {
            val input = ProcessInput(listOf("git", "log", "--oneline", "@{u}.."), directory)
            val output = processRunner.run(input)
            return expectNoOutputLines(input, output)
        }
    }

    object GitUnmergedChanges : Command {
        override fun runInDirectory(processRunner: ProcessRunner, directory: Path): CommandResult {
            val input = ProcessInput(listOf("git", "log", "--oneline", "..@{u}"), directory)
            val output = processRunner.run(input)
            return expectNoOutputLines(input, output)
        }
    }

    class CommandResult(val isSuccessful: Boolean, val input: ProcessInput, val output: ProcessOutput) {
        fun toMultipleLineString(): List<String> = listOf("CommandResult") + compose().indent()
        private fun compose(): List<String> = listOf("isSuccessful = $isSuccessful") + composeInput() + composeOutput()
        private fun composeInput(): List<String> = input.toMultipleLineString()
        private fun composeOutput(): List<String> = output.toMultipleLineString()
    }
}
