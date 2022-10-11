package com.seanshubin.kotlin.tryme.domain.config

import java.nio.file.Path
import java.time.Duration
import java.time.Instant

interface Configuration {
    fun intLoaderAt(default: Any?, keys: List<String>): () -> Int
    fun stringLoaderAt(default: Any?, keys: List<String>): () -> String
    fun pathLoaderAt(default: Any?, keys: List<String>): () -> Path
    fun instantLoaderAt(default: Any?, keys: List<String>): () -> Instant
    fun formattedSecondsLoaderAt(default: Any?, keys: List<String>): () -> Long
    fun stringListLoaderAt(default: Any?, keys: List<String>): () -> List<String>
    fun pathListLoaderAt(default: Any?, keys: List<String>): () -> List<Path>
}
