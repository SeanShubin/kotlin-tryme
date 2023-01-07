package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

interface HttpContract {
    fun getString(uri: URI):HttpStringResult
}