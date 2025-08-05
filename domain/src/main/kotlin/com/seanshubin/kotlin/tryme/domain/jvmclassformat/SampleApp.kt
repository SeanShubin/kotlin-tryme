package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Clock

object SampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        foo(args, System.out)
    }

    fun foo(args: Array<String>, output: PrintStream){
        val clock = Clock.systemUTC()
        val startTime = clock.instant()
        val fileName = args[0]
        val path = Paths.get(fileName)
        val charset = Charsets.UTF_8
        val target = Files.readString(path, charset)
        val greeting = "Hello, $target!"
        output.println(greeting)
        val endTime = clock.instant()
        val duration = endTime.toEpochMilli() - startTime.toEpochMilli()
        val timeTakenMessage = "Took $duration milliseconds"
        output.println(timeTakenMessage)
    }
}
