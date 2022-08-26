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

class JsonFileConfiguration(
    private val files: FilesContract,
    private val configFilePath: Path
) : Configuration {
    override fun intLoaderAt(default: Any?, vararg keys: String): () -> Int =
        genericLoader(IntConverter, default, *keys)

    override fun stringLoaderAt(default: Any?, vararg keys: String): () -> String =
        genericLoader(StringConverter, default, *keys)

    override fun pathLoaderAt(default: Any?, vararg keys: String): () -> Path =
        genericLoader(PathConverter, default, *keys)

    override fun instantLoaderAt(default: Any?, vararg keys: String): () -> Instant =
        genericLoader(InstantConverter, default, *keys)

    override fun formattedSecondsLoaderAt(default: Any?, vararg keys: String): () -> Long =
        genericLoader(DurationSecondsConverter, default, *keys)

    interface Converter<T> {
        val sourceType: Class<*>
        fun convert(value: Any?): T?
    }

    object StringConverter : Converter<String> {
        override val sourceType: Class<*> get() = String::class.java

        override fun convert(value: Any?): String? =
            if (value is String) value else null
    }

    object IntConverter : Converter<Int> {
        override val sourceType: Class<*> get() = Int::class.java

        override fun convert(value: Any?): Int? =
            if (value is Int) value else null
    }

    object PathConverter : Converter<Path> {
        override val sourceType: Class<*> get() = String::class.java

        override fun convert(value: Any?): Path? =
            if (value is String) Paths.get(value) else null
    }

    object InstantConverter : Converter<Instant> {
        override val sourceType: Class<*> get() = Instant::class.java

        override fun convert(value: Any?): Instant? =
            if (value is String) Instant.parse(value) else null
    }

    object DurationSecondsConverter : Converter<Long> {
        override val sourceType: Class<*> get() = String::class.java

        override fun convert(value: Any?): Long? =
            if (value is String) DurationFormat.seconds.parse(value) else null
    }

    private fun <T> genericLoader(converter: Converter<T>, default: Any?, vararg keys: String): () -> T = {
        val untyped = loadUntyped(default.toJsonType(), *keys)
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
            else -> {
                val typeName = this.javaClass.name
                throw RuntimeException("Don't know how to convert '$this' of type '$typeName' to a JSON type")
            }
        }

    private fun loadUntyped(default: Any?, vararg keys: String): Untyped {
        return if (files.exists(configFilePath)) {
            val text = files.readString(configFilePath)
            val untyped = Untyped(parser.readValue<Any?>(text))
            if (untyped.hasValueAtPath(*keys)) {
                Untyped(untyped.getValueAtPath(*keys))
            } else {
                val newUntyped = untyped.setValueAtPath(default, *keys)
                val jsonText = pretty.writeValueAsString(newUntyped.value)
                files.writeString(configFilePath, jsonText)
                loadUntyped(default, *keys)
            }
        } else {
            files.writeString(configFilePath, "{}")
            loadUntyped(default, *keys)
        }
    }
}