package com.seanshubin.kotlin.tryme.domain.http

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicReference

object ThrottleTestClientApp {
    @JvmStatic
    fun main(args: Array<String>) {
        var value = AtomicReference<IndexStatusBody>()
        var index = 0;
        fun updateValueIfLater(newValue: IndexStatusBody) {
            var done = false
            while(!done){
                val oldValue = value.get()
                done = if(oldValue == null || oldValue.index < newValue.index) {
                    println("Updating value from $oldValue to $newValue")
                    value.compareAndSet(oldValue, newValue)
                } else {
                    println("No need to update: $oldValue is later than $newValue")
                    true
                }
            }
        }
        fun updateValueRegardless(newValue: IndexStatusBody) {
            var done = false
            while (!done) {
                val oldValue = value.get()
                done = value.compareAndSet(oldValue, newValue)
            }
        }
        fun updateValue(newValue: IndexStatusBody) {
            updateValueIfLater(newValue)
//            updateValueRegardless(newValue)
        }

        val client = HttpClient.newHttpClient()
        val uriString = "http://localhost:${ThrottleTestConstants.port}"
        val uri = URI.create(uriString)
        val stringBodyHandler = HttpResponse.BodyHandlers.ofString()
        val futures = Collections.synchronizedList(ArrayList<Future<Void>>())
        fun send(delayMillis: Long, value: String) {
            val localIndex = ++index
            val requestBody = JsonMappers.pretty.writeValueAsString(
                mapOf(
                    "delayMillis" to delayMillis,
                    "value" to value
                )
            )
            val request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
            println("Sending request: $requestBody")
            val future = client.sendAsync(request, stringBodyHandler).thenAccept { response ->
                val statusCode = response.statusCode()
                val responseBody = response.body()
                val indexStatusBody = IndexStatusBody(localIndex, statusCode, responseBody)
                updateValue(indexStatusBody)
            }
            futures.add(future)
        }
        send(500, "a")
        Thread.sleep(50)
        send(100, "b")
        blockUntilComplete(futures)
        println(value.get())
    }

    private fun blockUntilComplete(futures: List<Future<Void>>) {
        futures.forEach { future ->
            try {
                future.get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    data class IndexStatusBody(val index: Int, val status: Int, val body: String)
}