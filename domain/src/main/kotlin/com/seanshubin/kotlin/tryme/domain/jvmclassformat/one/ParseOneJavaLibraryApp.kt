package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object ParseOneJavaLibraryApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val startTime = System.currentTimeMillis()
        val inputDir = Paths.get("generated", "jmods")
        val outputDir = Paths.get("generated", "jmod-report2")
        val summaryFile  = outputDir.resolve("summary.txt")
        Files.deleteIfExists(summaryFile)
        val summary = Summary()
        val events = object:Parser.Events{
            override fun parsingFile(file: Path, outputDir:Path) {
                println("Parsing $file -> $outputDir")
                summary.parsingFile(file)
            }

            override fun finishedApp(startTime: Long, endTime: Long) {
                Files.write(summaryFile, summary.lines(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                val duration = endTime - startTime
                val durationFormatted = DurationFormat.milliseconds.format(duration)
                val timeTakenLine = "Parsed ${summary.filesParsed} files in $durationFormatted"
                Files.write(summaryFile, listOf(timeTakenLine), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                println(timeTakenLine)
            }
        }
        val parser = Parser(inputDir, outputDir, events)
        parser.parseDir()
        val endTime = System.currentTimeMillis()
        events.finishedApp(startTime, endTime)
    }
}