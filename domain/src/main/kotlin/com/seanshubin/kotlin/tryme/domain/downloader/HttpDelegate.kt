package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpDelegate(private val httpClient:HttpClient):HttpContract {
    override fun getString(uri: URI): HttpStringResult {
        val request = HttpRequest.newBuilder().uri(uri).build()
        val bodyHandler = HttpResponse.BodyHandlers.ofString()
        val response = httpClient.send(request, bodyHandler)
        val statusCode = response.statusCode()
        val text = response.body()
        return HttpStringResult(statusCode, text)
    }
}
