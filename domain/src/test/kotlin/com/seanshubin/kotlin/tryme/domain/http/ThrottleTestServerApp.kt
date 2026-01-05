package com.seanshubin.kotlin.tryme.domain.http

import com.seanshubin.kotlin.tryme.domain.dynamic.JsonMappers
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.util.concurrent.Executors

object ThrottleTestServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        LocalDateTime.now().let { startTime ->
            println("Starting server at $startTime")
        }
        val server: HttpServer = HttpServer.create(InetSocketAddress(ClientServerConstants.port), 0)
        val myHandler = MyHandler()
        server.createContext("/", myHandler)
        val availableProcessors = Runtime.getRuntime().availableProcessors()
        val threads = availableProcessors
        val executor = Executors.newFixedThreadPool(threads)
        server.executor = executor
        println("Starting server with $threads threads")
        server.start();
    }

    class MyHandler : HttpHandler {
        override fun handle(exchange: HttpExchange) {
            println("Received request at ${LocalDateTime.now()} with method: ${exchange.requestMethod} and URI: ${exchange.requestURI}")
            when (exchange.requestMethod) {
                "OPTIONS" -> {
                    handleOptions(exchange)
                }

                "POST" -> {
                    handlePost(exchange)
                }

                else -> {
                    println("Unsupported request method: ${exchange.requestMethod}")
                    exchange.sendResponseHeaders(405, -1) // Method Not Allowed
                    exchange.responseBody.close()
                }
            }
        }
    }

    fun handleOptions(exchange: HttpExchange) {
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type")
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.sendResponseHeaders(200, 0)
        exchange.responseBody.close()
    }

    fun handlePost(exchange: HttpExchange) {
        println("request = ${exchange.requestMethod}")
        val bytes = exchange.requestBody.readAllBytes()
        val text = bytes.toString(Charset.forName("UTF-8"))
        println("Received request: $text")
        val map = JsonMappers.parser.readValue(text, Map::class.java)
        val delayMillis = map["delayMillis"] as Int
        val value = map["value"] as String
        val response = "response for $value"
        if (delayMillis > 0) {
            Thread.sleep(delayMillis.toLong())
        }
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type")
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
        exchange.sendResponseHeaders(200, 0)
        val outputStream = exchange.responseBody
        outputStream.write(response.toByteArray())
        outputStream.close()
        println("Sent response: $response")
    }
}