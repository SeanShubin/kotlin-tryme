package com.seanshubin.kotlin.tryme.domain.shell

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.concurrent.Executors

fun main() {
    val charset = StandardCharsets.UTF_8
    val outputLineEvent: (String) -> Unit = { println("OUT: $it") }
    val errorLineEvent: (String) -> Unit = { println("ERR: $it") }
    val directory = Paths.get("/Users/seanshubin/github/sean/utility/exec-test-app/target")
    val command = listOf("java", "-jar", "utility.jar", "alice", "bob", "carol", "dave")
    println("cd $directory")
    println(command.joinToString(" "))
    val processBuilder = ProcessBuilder()
    processBuilder.command(command)
    processBuilder.directory(directory.toFile())
    val process = processBuilder.start()
    val out = PrintWriter(OutputStreamWriter(process.outputStream))
    val threadPool = Executors.newCachedThreadPool()
    val coroutineDispatcher: CoroutineDispatcher = threadPool.asCoroutineDispatcher()
    GlobalScope.launch(coroutineDispatcher) {
        IoUtil.inputStreamToLineEvent(process.errorStream, charset, errorLineEvent)
    }
    GlobalScope.launch(coroutineDispatcher) {
        IoUtil.inputStreamToLineEvent(process.inputStream, charset, outputLineEvent)
    }
    out.println("err 4")
    out.println("out 2")
    out.println("exit 3")
    out.flush()
    process.waitFor()
    threadPool.shutdown()
}
