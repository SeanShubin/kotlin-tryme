package com.seanshubin.kotlin.tryme.domain.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.untyped.Untyped
import java.nio.file.Path

class JsonConfigFile(
    val files: FilesContract,
    val configFilePath: Path
) {
    fun loadInt(default: Any?, vararg pathParts: String): () -> Int = {
        toInt(loadUntyped(default, *pathParts),*pathParts)
    }

    fun loadString(default: Any?, vararg pathParts: String): () -> String = {
        toString(loadUntyped(default, *pathParts), *pathParts)
    }

    private fun toInt(untyped:Untyped, vararg pathParts:String):Int {
        when(val value = untyped.value){
            is Int -> return value
            else -> {
                val valueType = value?.javaClass?.simpleName ?: "null type"
                val pathString = pathParts.joinToString(".")
                val message = "At path $pathString, expected type Int, got $valueType for: $value"
                throw RuntimeException(message)
            }
        }
    }
    private fun toString(untyped:Untyped, vararg pathParts:String):String {
        when(val value = untyped.value){
            is String -> return value
            else -> {
                val valueType = value?.javaClass?.simpleName ?: "null type"
                val pathString = pathParts.joinToString(" > ")
                val message = "At path $pathString, expected type Int, got $valueType for: $value"
                throw RuntimeException(message)
            }
        }
    }

    private fun loadUntyped(default: Any?, vararg pathParts: String): Untyped {
        return if (files.exists(configFilePath)) {
            val text = files.readString(configFilePath)
            val untyped = Untyped(parser.readValue<Any?>(text))
            if (untyped.hasValueAtPath(*pathParts)) {
                Untyped(untyped.getValueAtPath(*pathParts))
            } else {
                val newUntyped = untyped.setValueAtPath(default, *pathParts)
                val jsonText = pretty.writeValueAsString(newUntyped.value)
                files.writeString(configFilePath, jsonText)
                loadUntyped(default, *pathParts)
            }
        } else {
            files.writeString(configFilePath, "{}")
            loadUntyped(default, *pathParts)
        }
    }


    companion object {
        val pretty: ObjectMapper = ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
        val compact: ObjectMapper = ObjectMapper().registerKotlinModule()
        val parser: ObjectMapper = compact
    }
}