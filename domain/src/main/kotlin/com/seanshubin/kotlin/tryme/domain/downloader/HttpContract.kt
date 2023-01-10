package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.nio.file.Path

interface HttpContract {
    fun getString(uri: URI, headers:List<Pair<String, String>>):HttpStringResult
    fun download(uri: URI, headers:List<Pair<String, String>>, path: Path)
}