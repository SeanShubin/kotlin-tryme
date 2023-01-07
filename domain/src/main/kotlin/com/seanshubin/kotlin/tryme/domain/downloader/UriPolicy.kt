package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

interface UriPolicy {
    val name:String
    fun accept(uri: URI):Boolean
}
