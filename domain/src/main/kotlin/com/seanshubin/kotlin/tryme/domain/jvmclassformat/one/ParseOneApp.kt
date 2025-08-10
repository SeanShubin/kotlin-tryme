package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths

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
        ParseUtil.parseFile(inputDir, outputDir, inputFile)
    }
}