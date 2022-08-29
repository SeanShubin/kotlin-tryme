package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.json.store.KeyValueStoreTestBase
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.pretty
import java.nio.file.Path
import java.nio.file.Paths

class JsonFileKeyValueStoreTest : KeyValueStoreTestBase() {
    override fun createStore(): KeyValueStore {
        val files = createFiles()
        val file = createFile(files)
        files.writeString(file, "")
        return JsonFileKeyValueStore(files, file)
    }

    override fun createStore(initialValue: Map<*, *>): KeyValueStore {
        val files = createFiles()
        val file = createFile(files)
        val initialText = pretty.writeValueAsString(initialValue)
        files.writeString(file, initialText)
        return JsonFileKeyValueStore(files, file)
    }

    private fun createFiles(): FilesContract = FilesFake()
    private fun createFile(files: FilesContract): Path {
        val file = Paths.get("generated", "json-file-key-value-store.json")
        files.createDirectories(file.parent)
        return file
    }
}
