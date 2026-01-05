package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.Files
import java.nio.file.Paths

object JsonFileKeyValueStoreArrayPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val pathName = "generated/json-file-key-value-store.json"
        val path = Paths.get(pathName)
        Files.deleteIfExists(path)
        val keyValueStore = FixedPathJsonFileKeyValueStore(FilesDelegate, path)
        keyValueStore.store(listOf("the-array", 0, "name"), "a")
        keyValueStore.store(listOf("the-array", 0, "value"), 1)
        keyValueStore.store(listOf("the-array", 1, "name"), "b")
        keyValueStore.store(listOf("the-array", 1, "value"), 2)
        keyValueStore.store(listOf("the-array", 2, "name"), "c")
        keyValueStore.store(listOf("the-array", 2, "value"), 3)
        println(keyValueStore.arraySize(listOf("the-array")))
        println(keyValueStore.load(listOf("the-array", 0, "name")))
        println(keyValueStore.load(listOf("the-array", 0, "value")))
        println(keyValueStore.load(listOf("the-array", 1, "name")))
        println(keyValueStore.load(listOf("the-array", 1, "value")))
        println(keyValueStore.load(listOf("the-array", 2, "name")))
        println(keyValueStore.load(listOf("the-array", 2, "value")))
    }
}

/*
{ "the-array": [
  { "name": "a", "value": 1 },
  { "name": "b", "value": 2 },
  { "name": "c", "value": 3 }
]
}
*/
