package com.seanshubin.kotlin.tryme.domain.config

import java.nio.file.Path

interface Configuration {
    fun intLoaderAt(default: Any?, vararg keys: String): () -> Int
    fun stringLoaderAt(default: Any?, vararg keys: String): () -> String
    fun pathLoaderAt(default:Any?, vararg keys:String):() -> Path
}
