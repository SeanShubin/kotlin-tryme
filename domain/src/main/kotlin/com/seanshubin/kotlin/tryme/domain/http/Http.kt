package com.seanshubin.kotlin.tryme.domain.http

interface Http {
    fun send(request: Request): Response
    fun sendAndFollowRedirects(request: Request): List<RequestResponse>
}
