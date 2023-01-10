package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class HttpDelegate(private val httpClient:HttpClient):HttpContract {
    override fun getString(uri: URI, headers:List<Pair<String, String>>): HttpStringResult {
        println("get: $uri")
        val bodyHandler = HttpResponse.BodyHandlers.ofString()
        val (statusCode, body) = get(uri, headers, bodyHandler)
        return HttpStringResult(statusCode, body)
    }

    override fun download(uri: URI, headers:List<Pair<String, String>>, path: Path) {
        val bodyHandler = HttpResponse.BodyHandlers.ofFile(path)
        get(uri, headers, bodyHandler)
    }

    private fun <T> get(uri:URI, headers:List<Pair<String, String>>, bodyHandler:HttpResponse.BodyHandler<T>):Pair<Int, T> {
        val requestBuilder = HttpRequest.newBuilder().uri(uri)
        headers.forEach { (key, value) ->
            requestBuilder.header(key, value)
        }
        val request = requestBuilder.build()
        val response = httpClient.send(request, bodyHandler)
        val statusCode = response.statusCode()
        return if (statusCode.isElsewhere()) {
            val newUri = URI(response.headers().map().getValue("location")[0])
            get(newUri, headers, bodyHandler)
        } else {
            val body = response.body()
            Pair(statusCode, body)
        }
    }

    private fun Int.isOk(): Boolean = this in 200..299
    private fun Int.isElsewhere(): Boolean = this in 300..399
}
