package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class FlexiblePathJsonFileKeyValueStore(val files: FilesContract):PathKeyValueStore {
    override fun load(path: Path, key: List<Any>): Any? {
        assertKeyValid(key)
        val jsonObject = loadJsonObject(path)
        return DynamicUtil.get(jsonObject, key)
    }

    override fun store(path: Path, key: List<Any>, value: Any?) {
        assertKeyValid(key)
        val jsonObject = loadJsonObject(path)
        val newJsonObject = DynamicUtil.set(jsonObject, key, value)
        val newJsonText = JsonMappers.pretty.writeValueAsString(newJsonObject)
        files.writeString(path, newJsonText, jsonCharset)
    }

    override fun exists(path: Path, key: List<Any>): Boolean {
        assertKeyValid(key)
        if (!files.exists(path)) return false
        val jsonObject = loadJsonObject(path)
        return DynamicUtil.exists(jsonObject, key)
    }

    override fun arraySize(path: Path, key: List<Any>): Int {
        assertKeyValid(key)
        val jsonObject = loadJsonObject(path)
        val array = DynamicUtil.get(jsonObject, key) as List<*>
        return array.size
    }

    private fun loadJsonObject(path: Path): Any? {
        val text = if(files.exists(path)) {
            files.readString(path, jsonCharset)
        } else {
            defaultJsonText
        }
        val jsonText = text.ifBlank { "{}" }
        val jsonObject = JsonMappers.parser.readValue<Any?>(jsonText)
        return jsonObject
    }

    private fun assertKeyValid(key: List<Any>) {
        key.forEach(::assertKeyPartValid)
    }

    private fun assertKeyPartValid(keyPart:Any){
        when (keyPart) {
            is String, is Int -> Unit
            else -> throw IllegalArgumentException("All key parts must be String or Int, got $keyPart")
        }
    }

    companion object Companion {
        private val jsonCharset = StandardCharsets.UTF_8
        private val defaultJsonText = "{}"
    }
}