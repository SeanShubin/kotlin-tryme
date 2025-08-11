package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
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
        val summaryFile = outputDir.resolve("summary.txt")
        Files.deleteIfExists(summaryFile)
        val events = object : Parser.Events {
            override fun parsingFile(file: java.nio.file.Path, outputDir: java.nio.file.Path) {
                println("Parsing $file -> $outputDir")
            }

            override fun finishedApp(startTime: Long, endTime: Long) {
                throw UnsupportedOperationException("Not Implemented!")
            }
        }
        val profiler = Profiler()
        val parser = Parser(inputDir, outputDir, events)
        parser.parseFile(inputFile)
    }
}