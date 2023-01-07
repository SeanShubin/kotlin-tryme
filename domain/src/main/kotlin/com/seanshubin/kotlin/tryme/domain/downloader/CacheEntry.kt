package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.time.Instant

data class CacheEntry(
    val uri: URI,
    val whenCached: Instant,
    val statusCode: Int,
    val relativePath: String
)