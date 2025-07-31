package com.seanshubin.kotlin.tryme.domain.http

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object HttpPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val client = HttpClient.newHttpClient()
        val uriString = "https://www.google.com"
        val uri = URI.create(uriString)
        val request = HttpRequest.newBuilder().uri(uri).build()
        val stringBodyHandler = HttpResponse.BodyHandlers.ofString()
        val response = client.send(request, stringBodyHandler)
        val statusCode = response.statusCode()
        val body = response.body()
        println(statusCode)
        println(body)
    }
}
