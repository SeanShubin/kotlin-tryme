package com.seanshubin.kotlin.tryme.domain.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.untyped.Untyped
import java.nio.file.Path
import java.nio.file.Paths

class JsonFileConfiguration(
    private val files: FilesContract,
    private val configFilePath: Path
):Configuration {
    override fun intLoaderAt(default: Any?, vararg keys: String): () -> Int = {
        toInt(loadUntyped(default, *keys),*keys)
    }

    override fun stringLoaderAt(default: Any?, vararg keys: String): () -> String = {
        toString(loadUntyped(default, *keys), *keys)
    }

    override fun pathLoaderAt(default:Any?, vararg keys:String):() -> Path = {
        toPath(loadUntyped(default, *keys), *keys)
    }

    private fun toInt(untyped:Untyped, vararg keys:String):Int {
        when(val value = untyped.value){
            is Int -> return value
            else -> {
                val valueType = value?.javaClass?.simpleName ?: "null type"
                val pathString = keys.joinToString(".")
                val message = "At path $pathString, expected type Int, got $valueType for: $value"
                throw RuntimeException(message)
            }
        }
    }
    private fun toString(untyped:Untyped, vararg keys:String):String {
        when(val value = untyped.value){
            is String -> return value
            else -> {
                val valueType = value?.javaClass?.simpleName ?: "null type"
                val pathString = keys.joinToString(" > ")
                val message = "At path $pathString, expected type Int, got $valueType for: $value"
                throw RuntimeException(message)
            }
        }
    }

    private fun toPath(untyped:Untyped, vararg keys:String):Path {
        when(val value = untyped.value){
            is Path -> return value
            is String -> return Paths.get(value)
            else -> {
                val valueType = value?.javaClass?.simpleName ?: "null type"
                val pathString = keys.joinToString(" > ")
                val message = "At path $pathString, expected type Path, got $valueType for: $value"
                throw RuntimeException(message)
            }
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


    companion object {
        val pretty: ObjectMapper = ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
        val compact: ObjectMapper = ObjectMapper().registerKotlinModule()
        val parser: ObjectMapper = compact
    }
}