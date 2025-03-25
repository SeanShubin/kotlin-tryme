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
        return addObjectAtPath(emptyList(), o)
    }

    private fun addObjectAtPath(path: List<String>, o: Any?): DataStructureAnalyzer {
        return when (o) {
            null -> incrementType(path, TypeEnum.NULL)
            is String -> addStringAtPath(path, o)
            is Int -> incrementType(path, TypeEnum.INT)
            is Boolean -> incrementType(path, TypeEnum.BOOLEAN)
            is Double -> incrementType(path, TypeEnum.DOUBLE)
            is List<*> -> addListAtPath(path, o)
            is Map<*, *> -> addMapAtPath(path, o)
            else -> throw UnsupportedOperationException("Unsupported type ${o.javaClass.name}")
        }
    }

    private fun addStringAtPath(path: List<String>, s: String): DataStructureAnalyzer {
        try {
            val o = parseJson(s)
            return incrementType(path, TypeEnum.NESTED_JSON).addObjectAtPath(path, o)
        } catch (e: JsonParseException) {
            return incrementType(path, TypeEnum.STRING)
        } catch(e: MismatchedInputException){
            return incrementType(path, TypeEnum.STRING)
        }
    }

    private fun addMapAtPath(path: List<String>, o: Map<*, *>): DataStructureAnalyzer {
        var current = this
        o.forEach { (key, value) ->
            key as String
            current = current.addObjectAtPath(path + key, value)
        }
        return current
    }

    private fun incrementType(path: List<String>, typeEnum: TypeEnum): DataStructureAnalyzer {
        return DataStructureAnalyzer(twoKeyMap.update(path.pathAsString(), typeEnum.name, increment, 0))
    }

    private fun addListAtPath(path: List<String>, o: List<Any?>): DataStructureAnalyzer {
        var current = this
        o.forEachIndexed { index, value ->
            current = current.addObjectAtPath(path + "[]", value)
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
        BOOLEAN,
        NESTED_JSON
    }

    companion object {
        val empty = DataStructureAnalyzer(empty())
    }
}
