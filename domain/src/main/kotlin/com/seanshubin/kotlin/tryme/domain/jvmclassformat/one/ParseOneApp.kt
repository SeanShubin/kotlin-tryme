package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object ParseOneApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get(
            "domain",
            "target",
            "classes",
            "com",
            "seanshubin",
            "kotlin",
            "tryme",
            "domain",
            "jvmclassformat",
            "samples"
        )
        val inputFile = inputDir.resolve("Sample1.class")
        val outputDir = Paths.get("generated", "sample")
        val events = object : ParseUtil.Events {
            override fun timeTaken(millis: Long) {
                val formattedTime = DurationFormat.milliseconds.format(millis)
                Files.write(
                    outputDir.resolve("summary.txt"),
                    listOf("Time taken: $formattedTime"),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
                )
            }

            override fun parsingFile(file: java.nio.file.Path, outputDir: java.nio.file.Path) {
                println("Parsing $file -> $outputDir")
            }
        }
        ParseUtil.parseFile(inputDir, outputDir, inputFile, events)
    }
}