package com.seanshubin.kotlin.tryme.domain.downloader

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.dynamic.JsonMappers
import java.nio.file.Path

class PersistenceToFile(
    private val baseDir: Path,
    private val files:FilesContract
):Persistence {
    override fun addToSet(name: String, value: String) {
        addToSet(name, listOf(value))
    }

    override fun addToSet(name: String, values: List<String>) {
        val path = baseDir.resolve("$name.json")
        initializeStringArrayIfNeeded(path)
        val existingText =  files.readString(path)
        val existingSet = JsonMappers.parser.readValue<List<String>>(existingText)
        val newSet = (existingSet + values).distinct().sorted()
        val newText = JsonMappers.pretty.writeValueAsString(newSet)
        files.writeString(path, newText)
    }

    private fun initializeStringArrayIfNeeded(path:Path){
        if(files.exists(path)) return
        FilesUtil.ensureParentExists(files, path)
        val text = JsonMappers.pretty.writeValueAsString(emptyList<String>())
        files.writeString(path, text)
    }
}
