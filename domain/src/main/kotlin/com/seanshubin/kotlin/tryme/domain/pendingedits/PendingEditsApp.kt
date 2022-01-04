package com.seanshubin.kotlin.tryme.domain.pendingedits

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

object PendingEditsApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val home = System.getProperty("user.home")
        val homePath = Paths.get(home)
        val paths = args.map { homePath.resolve(it) }
        pendingEditsForDirectories(0, paths)
    }

    fun pendingEditsForDirectories(depth: Int, paths: List<Path>) {
        paths.forEach { pendingEditsForPath(depth, it) }
    }

    fun pendingEditsForPath(depth: Int, path: Path) {
        val gitPath = path.resolve(".git")
        if (Files.exists(gitPath)) {
            val statusLines = execLines(path, "git status -s")
            val localCommitLines = execLines(path, "git log --oneline @{u}..")
            val lines = statusLines + localCommitLines
            if (lines.isNotEmpty()) {
                println("PENDING EDITS: $path")
                lines.map { "  $it" }.forEach(::println)
            }
        } else {
            if (depth > 2) throw RuntimeException("Too deep at $path")
            val directories = (Files.list(path)?.toList() ?: emptyList<Path>()).filter(::acceptPath)
            pendingEditsForDirectories(depth + 1, directories)
        }
    }

    fun acceptPath(path: Path): Boolean {
        if (!Files.isDirectory(path)) return false
        if (path.fileName.toString().startsWith(".")) return false
        return true
    }

    val whitespace = Regex("""\s+""")

    fun execLines(path: Path, commands: String): List<String> {
        val processBuilder = ProcessBuilder()
        processBuilder.command(*commands.split(whitespace).toTypedArray())
        processBuilder.directory(path.toFile())
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        process.waitFor()
        return reader.lines().toList()
    }
}
