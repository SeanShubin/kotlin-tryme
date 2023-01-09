package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.nio.file.Path

interface HttpContract {
    fun getString(uri: URI):HttpStringResult
    fun download(uri: URI, path: Path)
}