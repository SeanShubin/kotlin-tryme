package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import java.io.DataInputStream
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
        val events = object:ParseUtil.Events{
            override fun timeTaken(millis: Long) {
                val formattedTime = DurationFormat.milliseconds.format(millis)
                Files.write(summaryFile, listOf("Time taken: $formattedTime"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            }

            override fun parsingFile(file: Path, outputDir:Path) {
                println("Parsing $file -> $outputDir")
            }
        }
        ParseUtil.parseDir(inputDir, outputDir, events)
    }
}