package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.nio.file.Files.readString
import java.nio.file.Paths.get
import java.time.Clock.systemUTC

object SampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val clock = systemUTC()
        val startTime = clock.instant()
        val fileName = args[0]
        val path = get(fileName)
        val charset = Charsets.UTF_8
        val target = readString(path, charset)
        val greeting = "Hello, $target!"
        System.out.println(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        System.out.println(timeTakenMessage)
    }
}
