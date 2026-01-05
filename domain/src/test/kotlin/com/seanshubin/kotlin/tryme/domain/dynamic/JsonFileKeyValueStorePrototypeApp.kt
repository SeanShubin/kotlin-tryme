package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.Paths

object JsonFileKeyValueStorePrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val pathName = "generated/json-file-key-value-store.json"
        val path = Paths.get(pathName)
        val keyValueStore = FixedPathJsonFileKeyValueStore(path, FilesDelegate)
        keyValueStore.store(listOf("key1", "key2", "key3"), "value1")
    }
}