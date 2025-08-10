package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths

object ParseOneApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputFile = Paths.get(
            "domain",
            "target",
            "classes",
            "com",
            "seanshubin",
            "kotlin",
            "tryme",
            "domain",
            "jvmclassformat",
            "samples",
            "Sample1.class"
        )
        val jvmClassInfo = DataInputStream(Files.newInputStream(inputFile)).use { inputStream ->
            JvmClassInfo.fromDataInput(inputStream)
        }
        jvmClassInfo.lines().forEach(::println)
    }
}