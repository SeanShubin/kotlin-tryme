package com.seanshubin.kotlin.tryme.domain.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


object ServerPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val server: HttpServer = HttpServer.create(InetSocketAddress(ClientServerConstants.port), 0)
        val seed = System.currentTimeMillis()
        val random = Random(seed)
        val myHandler = MyHandler(random)
        server.createContext(ClientServerConstants.path, myHandler)
        val availableProcessors = Runtime.getRuntime().availableProcessors()
        val threads = availableProcessors
        val executor = Executors.newFixedThreadPool(threads)
        server.executor = executor
        println("Starting server with $threads threads")
        server.start();
    }

    class MyHandler(val random: Random) : HttpHandler {
        var x:AtomicInteger = AtomicInteger(0)
        var inProgressCount = AtomicInteger(0)
        override fun handle(exchange: HttpExchange) {
            inProgressCount.incrementAndGet()
            val threadId = Thread.currentThread().threadId()
            val value = x.incrementAndGet()
            val delayMillis = random.nextInt(1000).toLong()
            println("Start in progress = $inProgressCount, call = $value, delay = $delayMillis, threadId = $threadId")
            Thread.sleep(delayMillis)
            val response = "response: in progress = $inProgressCount, call = $value, delay = $delayMillis, threadId = $threadId"
            exchange.sendResponseHeaders(200, response.length.toLong())
            val outputStream = exchange.responseBody
            outputStream.write(response.toByteArray())
            outputStream.close()
            println("Stop in progress = $inProgressCount, call = $value, delay = $delayMillis, threadId = $threadId")
            inProgressCount.decrementAndGet()
        }
    }
}
