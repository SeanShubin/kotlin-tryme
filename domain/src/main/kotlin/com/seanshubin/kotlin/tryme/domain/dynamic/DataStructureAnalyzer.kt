package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.dynamic.TwoKeyMap.Companion.empty
import com.seanshubin.kotlin.tryme.domain.dynamic.TwoKeyMap.Companion.increment
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import javax.xml.crypto.Data

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
            true -> incrementType(path, TypeEnum.BOOLEAN)
            is List<*> -> addListAtPath(path, o)
            is Map<*, *> -> addMapAtPath(path, o)
            is Int -> incrementType(path, TypeEnum.INT)
            is String -> addStringAtPath(path, o)
            is Boolean -> incrementType(path, TypeEnum.BOOLEAN)
            is Double -> incrementType(path, TypeEnum.DOUBLE)
            else -> throw UnsupportedOperationException("Unsupported type ${o.javaClass.name}")
        }
    }

    private fun addStringAtPath(path: List<String>, s: String): DataStructureAnalyzer {
        if(s == "null"){
            return incrementType(path, TypeEnum.NULL_AS_STRING)
        } else if (s == "true" || s == "false") {
            return incrementType(path, TypeEnum.BOOLEAN_AS_STRING)
        } else {
        try {
            s.toInt()
            return incrementType(path, TypeEnum.INT_AS_STRING)
        } catch (e: NumberFormatException) {
            try {
                s.toDouble()
                return incrementType(path, TypeEnum.DOUBLE_AS_STRING)
            } catch (e: NumberFormatException) {
                    try {
                        val o = parseJson(s)
                        return incrementType(path, TypeEnum.OBJECT_AS_STRING).addObjectAtPath(path, o)
                    } catch (e: JsonParseException) {
                        return incrementType(path, TypeEnum.STRING)
                    }
                }
            }
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
            current = current.addObjectAtPath(path + "[", value)
        }
        return current
    }

    private fun addDiscreteAtPath(path: List<String>, value: Any?): DataStructureAnalyzer {
        throw UnsupportedOperationException("not implemented")
    }

//    private fun getType(o: Any?): TypeEnum {
//        if (o == null) return TypeEnum.NULL
//        if (o is String) return getStringType(o)
//        if (o is Int) return TypeEnum.INT
//        throw UnsupportedOperationException("unsupported type: ${o.javaClass.typeName}")
//    }

//    private fun parseJson(s: String): JsonParseResult =
//        try {
//            JsonParseResult.Success(parseJsonSuccess(s))
//        } catch (ex: JsonParseException) {
//            println(s)
//            JsonParseResult.Failure(ex)
//        }

    private fun parseJson(s: String): Any? =
        JsonMappers.parser.readValue<Any?>(s)

//    private fun getStringType(s: String): TypeEnum {
//        val result = parseJson(s)
//        if (result is JsonParseResult.Success) {
//            return getType(result.value)
//        }
//        throw UnsupportedOperationException("not implemented")
//    } catch (x: JsonParseException)
//    {
//        return TypeEnum.STRING
//    }
//}

    private fun List<String>.pathAsString(): String = joinToString(".")

    companion object {
        val empty = DataStructureAnalyzer(empty())
        private val nullType = "Nothing?"
    }
}