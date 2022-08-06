package com.seanshubin.kotlin.tryme.domain.config

import java.nio.file.Path
import java.time.Duration
import java.time.Instant

interface Configuration {
    fun intLoaderAt(default: Any?, vararg keys: String): () -> Int
    fun stringLoaderAt(default: Any?, vararg keys: String): () -> String
    fun pathLoaderAt(default: Any?, vararg keys: String): () -> Path
    fun instantLoaderAt(default: Any?, vararg keys: String): () -> Instant
    fun formattedSecondsLoaderAt(default:Any?, vararg keys:String): () -> Long
}
