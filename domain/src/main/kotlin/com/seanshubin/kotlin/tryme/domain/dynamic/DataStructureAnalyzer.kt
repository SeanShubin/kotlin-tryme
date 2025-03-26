package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.dynamic.TwoKeyMap.Companion.empty
import com.seanshubin.kotlin.tryme.domain.dynamic.TwoKeyMap.Companion.increment
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

class DataStructureAnalyzer(private val twoKeyMap: TwoKeyMap<String, String, Int>) {
    val map: Map<String, Map<String, Int>> get() = twoKeyMap.level1
    fun addJson(json: String): DataStructureAnalyzer {
        val o = parseJson(json)
        return addObject(o)
    }

    fun addObject(o: Any?): DataStructureAnalyzer {
        return addObjectAtPath(emptyList(), o, 0)
    }

    private fun addObjectAtPath(path: List<String>, o: Any?, nestingLevel:Int): DataStructureAnalyzer {
        return when (o) {
            null -> incrementType(path, TypeEnum.NULL, nestingLevel)
            is String -> addStringAtPath(path, o, nestingLevel)
            is Int -> incrementType(path, TypeEnum.INT, nestingLevel)
            is Boolean -> incrementType(path, TypeEnum.BOOLEAN, nestingLevel)
            is Double -> incrementType(path, TypeEnum.DOUBLE, nestingLevel)
            is List<*> -> addListAtPath(path, o, nestingLevel)
            is Map<*, *> -> addMapAtPath(path, o, nestingLevel)
            else -> throw UnsupportedOperationException("Unsupported type ${o.javaClass.name}")
        }
    }

    private fun addStringAtPath(path: List<String>, s: String, nestingLevel:Int): DataStructureAnalyzer {
        try {
            val o = parseJson(s)
            return addObjectAtPath(path, o, nestingLevel+1)
        } catch (e: JsonParseException) {
            return incrementType(path, TypeEnum.STRING, nestingLevel)
        } catch(e: MismatchedInputException){
            return incrementType(path, TypeEnum.STRING, nestingLevel)
        }
    }

    private fun addMapAtPath(path: List<String>, o: Map<*, *>, nestingLevel:Int): DataStructureAnalyzer {
        var current = this
        o.forEach { (key, value) ->
            key as String
            current = current.addObjectAtPath(path + key, value, nestingLevel)
        }
        return current
    }

    private fun incrementType(path: List<String>, typeEnum: TypeEnum, nestingLevel:Int): DataStructureAnalyzer {
        val typeName = when(nestingLevel) {
            0 -> typeEnum.name
            1 -> "$typeEnum wrapped in string"
            else -> "$typeEnum wrapped in string $nestingLevel times"
        }
        return DataStructureAnalyzer(twoKeyMap.update(path.pathAsString(), typeName, increment, 0))
    }

    private fun addListAtPath(path: List<String>, o: List<Any?>, nestingLevel:Int): DataStructureAnalyzer {
        var current = this
        o.forEachIndexed { index, value ->
            current = current.addObjectAtPath(path + "[]", value, nestingLevel)
        }
        return current
    }

    private fun parseJson(s: String): Any? = JsonMappers.parser.readValue<Any?>(s)

    private fun List<String>.pathAsString(): String = joinToString(".")

    private enum class TypeEnum {
        NULL,
        STRING,
        INT,
        DOUBLE,
        BOOLEAN
    }

    companion object {
        val empty = DataStructureAnalyzer(empty())
    }
}
