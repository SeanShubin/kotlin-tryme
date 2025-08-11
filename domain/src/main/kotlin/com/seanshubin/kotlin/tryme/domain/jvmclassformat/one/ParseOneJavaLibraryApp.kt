package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object ParseOneJavaLibraryApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get("generated", "jmods")
        val outputDir = Paths.get("generated", "jmod-report2")
        val summaryFile  = outputDir.resolve("summary.txt")
        val summary = Summary()
        val events = object:Parser.Events{
            override fun parsingFile(file: Path, outputDir:Path) {
                println("Parsing $file -> $outputDir")
                summary.parsingFile(file)
            }

            override fun finishedDir(profiler: Profiler) {
                Files.write(summaryFile,profiler.lines(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                Files.write(summaryFile, summary.lines(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            }
        }
        val profiler = Profiler()
        val parser = Parser(inputDir, outputDir, events, profiler)
        parser.parseDir()
    }
}