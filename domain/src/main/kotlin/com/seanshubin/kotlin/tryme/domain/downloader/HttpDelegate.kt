package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class HttpDelegate(private val httpClient:HttpClient):HttpContract {
    override fun getString(uri: URI): HttpStringResult {
        val bodyHandler = HttpResponse.BodyHandlers.ofString()
        val (statusCode, body) = get(uri, bodyHandler)
        return HttpStringResult(statusCode, body)
    }

    override fun download(uri: URI, path: Path) {
        println("uri  = $uri")
        println("path = $path")
        val bodyHandler = HttpResponse.BodyHandlers.ofFileDownload(
            path,
            StandardOpenOption.CREATE_NEW
        )
        get(uri, bodyHandler)
    }

    private fun <T> get(uri:URI, bodyHandler:HttpResponse.BodyHandler<T>):Pair<Int, T> {
        val request = HttpRequest.newBuilder().uri(uri).build()
        val response = httpClient.send(request, bodyHandler)
        val statusCode = response.statusCode()
        val body = response.body()
        return Pair(statusCode, body)
    }
}
