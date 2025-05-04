package com.seanshubin.kotlin.tryme.domain.dynamic

import java.nio.file.Path
import java.nio.file.Paths

object TypeUtil {
    fun Any?.coerceToString(): String = this as String
    fun Any?.coerceToListOfString(): List<String> = (this as List<*>).map { it as String }
    fun Any?.coerceToInt(): Int = when (this) {
        is Int -> this
        is String -> Integer.parseInt(this)
        else -> failCoerce(this, Int::class.java)
    }

    fun Any?.coerceToPath(): Path = when (this) {
        is String -> Paths.get(this)
        is Path -> this
        else -> failCoerce(this, Path::class.java)
    }

    fun Any?.coerceToBoolean(): Boolean = when (this) {
        is String -> this.toBoolean()
        is Boolean -> this
        else -> failCoerce(this, Boolean::class.java)
    }

    private fun failCoerceMessage(fromValue: Any?, toType: Class<*>): String {
        val fromDescription = when (fromValue) {
            null -> "<null>"
            else -> "$fromValue of type ${fromValue.javaClass.name}"
        }
        val typeDescription = toType.typeName
        return "Unable to convert $fromDescription to $typeDescription"
    }

    private fun failCoerce(fromValue: Any?, toType: Class<*>): Nothing {
        val message = failCoerceMessage(fromValue, toType)
        throw RuntimeException(message)
    }
}
