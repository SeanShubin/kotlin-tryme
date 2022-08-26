package com.seanshubin.kotlin.tryme.domain.json.editor

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.parser
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.pretty
import com.seanshubin.kotlin.tryme.domain.untyped.Untyped
import java.nio.file.Path

class JsonFileKeyValueStore(
    val files: FilesContract,
    val file: Path
) : KeyValueStore {
    override val text: String get() = files.readString(file)

    override fun <T> setValue(value: T, vararg keys: String) {
        val oldUntyped = untyped(text)
        val newUntyped = oldUntyped.setValueAtPath(value, *keys)
        files.writeString(file,pretty.writeValueAsString(newUntyped.value))
    }

    override fun <T> getValue(converter: Converter<T>, vararg keys: String): T {
        val untyped = untyped(text)
        val value = untyped.getValueAtPath(*keys) ?: throwNullException(keys)
        return converter.convert(value)
    }

    override fun valueExists(vararg keys: String): Boolean {
        val untyped = untyped(text)
        return untyped.hasValueAtPath(*keys)
    }

    private fun untyped(text: String): Untyped {
        val jsonText = text.ifBlank { "{}" }
        val jsonObject: Any? = parser.readValue(jsonText)
        val untyped = Untyped(jsonObject)
        return untyped
    }

    private fun throwNullException(keys: Array<out String>): Nothing {
        val path = keys.joinToString(" -> ")
        throw RuntimeException("Null found at path $path")
    }
}