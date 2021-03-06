package com.seanshubin.kotlin.tryme.domain.process

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.timer.TimerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Predicate
import java.util.stream.Collectors

class DirectoryResult(val directory: Path, val commandResults: List<Command.CommandResult>) {
    fun toMultipleLineString(): List<String> = listOf("DirectoryResult") + compose().indent()
    fun hasUnsuccessfulCommand(): Boolean = commandResults.any { !it.isSuccessful }
    private fun compose(): List<String> = listOf("directory = $directory") + composeCommandResults()
    private fun composeCommandResults(): List<String> =
        listOf("commandResults") + commandResults.flatMap { it.toMultipleLineString() }.indent()
}

object AcceptDirectory : Predicate<Path> {
    override fun test(path: Path): Boolean {
        return when {
            !Files.isDirectory(path) -> false
            else -> true
        }
    }
}

fun getDirectories(basePath: Path): List<Path> {
    val ignore = listOf<String>()
    val notIgnored = { path: Path -> !ignore.contains(path.fileName.toString()) }
    return Files.list(basePath).filter(AcceptDirectory).filter(notIgnored).collect(Collectors.toList())
}

fun getCommands(): List<Command> = listOf(
        Command.GitFetch,
        Command.GitStatus,
        Command.GitLocalChanges,
        Command.GitUnmergedChanges)

fun runCommandInDirectory(processRunner: ProcessRunner, command: Command, directory: Path): Command.CommandResult =
    command.runInDirectory(processRunner, directory)

fun runCommandsInDirectory(processRunner: ProcessRunner, commands: List<Command>, directory: Path): DirectoryResult {
    println("processing: $directory")
    val commandResults = commands.map { command -> runCommandInDirectory(processRunner, command, directory) }
    val directoryResult = DirectoryResult(directory, commandResults)
    println("finished: $directory")
    return directoryResult
}

fun runCommandsInDirectories(
    processRunner: ProcessRunner,
    commands: List<Command>,
    directories: List<Path>
): List<DirectoryResult> {
    return directories.mapAsyncFixedThreadPool(4) { directory ->
        runCommandsInDirectory(processRunner, commands, directory)
    }
}

fun isNoteworthy(result: DirectoryResult): Boolean = result.hasUnsuccessfulCommand()

fun main(args: Array<String>) {
    val timer = TimerFactory.createDefault()
    val (duration, _) = timer.durationAndResult {
        val basePath: Path = Paths.get(args[0])
        val directories = getDirectories(basePath)
        val commands = getCommands()
        val processRunner: ProcessRunner = SystemProcessRunner()
        val results = runCommandsInDirectories(processRunner, commands, directories)
        val noteworthyResults = results.filter(::isNoteworthy)
        val lines = noteworthyResults.flatMap(DirectoryResult::toMultipleLineString)
        lines.forEach(::println)
        noteworthyResults.map(DirectoryResult::directory).forEach(::println)
        println(noteworthyResults.size)
    }
    println(DurationFormat.milliseconds.format(duration.toMillis()))
}
