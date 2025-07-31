package com.seanshubin.kotlin.tryme.domain.http

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import java.util.concurrent.Future
import kotlin.collections.ArrayList

object ClientPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val quantity = 100
        val client = HttpClient.newHttpClient()
        val uriString = "http://localhost:${ClientServerConstants.port}${ClientServerConstants.path}"
        println(uriString)
        val uri = URI.create(uriString)
        val request = HttpRequest.newBuilder().uri(uri).GET().build()
        val stringBodyHandler = HttpResponse.BodyHandlers.ofString()
        val futures = Collections.synchronizedList(ArrayList<Future<HttpResponse<String>>>())
        for (i in 0 until quantity) {
            val responseFuture = client.sendAsync(request, stringBodyHandler)
            responseFuture.thenAccept { response ->
                val statusCode = response.statusCode()
                val body = response.body()
                println("$statusCode = $body")
            }
            futures.add(responseFuture)
        }
        println("Sent all futures")
        val start = System.currentTimeMillis()
        blockUntilComplete(futures)
        val end = System.currentTimeMillis()
        val elapsed = end - start
        println("Elapsed ${elapsed / 1000.0} seconds")
    }
    private fun blockUntilComplete(futures: List<Future<HttpResponse<String>>>) {
        futures.forEach { future ->
            try {
                future.get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
