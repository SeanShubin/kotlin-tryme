package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.nio.file.Files
import java.nio.file.Paths
import java.time.Clock

object SampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val clock = Clock.systemUTC()
        val startTime = clock.instant()
        val fileName = args[0]
        val path = Paths.get(fileName)
        val charset = Charsets.UTF_8
        val target = Files.readString(path, charset)
        val greeting = "Hello, $target!"
        println(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        println(timeTakenMessage)
    }
}