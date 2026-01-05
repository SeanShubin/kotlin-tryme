package com.seanshubin.kotlin.tryme.domain.dynamic

import java.nio.file.Path

interface PathKeyValueStore {
    fun load(path: Path, key: List<Any>): Any?
    fun store(path: Path, key: List<Any>, value: Any?)
    fun exists(path: Path, key: List<Any>): Boolean
    fun arraySize(path: Path, key: List<Any>): Int
}
