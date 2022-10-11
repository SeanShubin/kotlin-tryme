package com.seanshubin.kotlin.tryme.domain.config

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.parser
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil.pretty
import com.seanshubin.kotlin.tryme.domain.untyped.Untyped
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import com.seanshubin.kotlin.tryme.domain.config.Converters.IntConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.StringConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.PathConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.InstantConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.DurationSecondsConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.StringListConverter
import com.seanshubin.kotlin.tryme.domain.config.Converters.PathListConverter

class JsonFileConfiguration(
    private val files: FilesContract,
    private val configFilePath: Path
) : Configuration {
    override fun intLoaderAt(default: Any?, keys: List<String>): () -> Int =
        genericLoader(IntConverter, default, keys)

    override fun stringLoaderAt(default: Any?, keys: List<String>): () -> String =
        genericLoader(StringConverter, default, keys)

    override fun pathLoaderAt(default: Any?, keys: List<String>): () -> Path =
        genericLoader(PathConverter, default, keys)

    override fun instantLoaderAt(default: Any?, keys: List<String>): () -> Instant =
        genericLoader(InstantConverter, default, keys)

    override fun formattedSecondsLoaderAt(default: Any?, keys: List<String>): () -> Long =
        genericLoader(DurationSecondsConverter, default, keys)

    override fun stringListLoaderAt(default: Any?, keys: List<String>): () -> List<String> =
        genericLoader(StringListConverter, default, keys)

    override fun pathListLoaderAt(default: Any?, keys: List<String>): () -> List<Path> =
        genericLoader(PathListConverter, default, keys)

    private fun <T> genericLoader(converter: Converter<T>, default: Any?, keys: List<String>): () -> T = {
        val untyped = loadUntyped(default.toJsonType(), keys)
        val value = untyped.value
        val typed = converter.convert(value)
        if (typed == null) {
            val expectedType = converter.sourceType.simpleName
            val valueType = value?.javaClass?.simpleName ?: "null type"
            val pathString = keys.joinToString(".")
            val message = "At path $pathString, expected type $expectedType, got $valueType for: $value"
            throw RuntimeException(message)
        } else {
            typed
        }
    }

    private fun Any?.toJsonType(): Any? =
        when (this) {
            null -> null
            is String -> this
            is Int -> this
            is Long -> this
            is Path -> this.toString()
            is Instant -> this.toString()
            is List<*> -> this
            else -> {
                val typeName = this.javaClass.name
                throw RuntimeException("Don't know how to convert '$this' of type '$typeName' to a JSON type")
            }
        }

    private fun loadUntyped(default: Any?, keys: List<String>): Untyped {
        return if (files.exists(configFilePath)) {
            val text = files.readString(configFilePath)
            val untyped = Untyped(parser.readValue<Any?>(text))
            if (untyped.hasValueAtPath(*keys.toTypedArray())) {
                Untyped(untyped.getValueAtPath(*keys.toTypedArray()))
            } else {
                val newUntyped = untyped.setValueAtPath(default, *keys.toTypedArray())
                val jsonText = pretty.writeValueAsString(newUntyped.value)
                files.writeString(configFilePath, jsonText)
                loadUntyped(default, keys)
            }
        } else {
            files.writeString(configFilePath, "{}")
            loadUntyped(default, keys)
        }
    }
}
