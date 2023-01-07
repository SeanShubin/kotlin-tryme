package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

interface LinkParser {
    fun parseLinks(source:URI, text:String):List<URI>
}
