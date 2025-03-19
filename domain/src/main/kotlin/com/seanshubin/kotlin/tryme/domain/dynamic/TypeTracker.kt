package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.dynamic.TwoKeyMap.Companion.increment
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

data class TypeTracker(
    val keyTypeQuantity: TwoKeyMap<String, String, Int>
) {
    fun addJson(s:String):TypeTracker {
        val o = JsonMappers.parser.readValue<Map<String, Any?>>(s)
        return addObject(o)
    }
    fun addObject(o:Map<String, Any?>):TypeTracker{
        return o.map { (key,value) ->
            val typeName = value?.javaClass?.simpleName ?: "null"
            Pair(key, typeName)
        }.fold(this, ::addType)
    }
    fun addType(key:String, typeName:String):TypeTracker {
        val newKeyTypeQuantity = keyTypeQuantity.update(key, typeName, increment, 0)
        return TypeTracker(newKeyTypeQuantity)
    }
    fun dataClass(name:String):String{
        val fieldList = keyTypeQuantity.level1.map { (key1, level2) ->
            field(key1, level2)
        }
        val fields = fieldList.joinToString(",\n", "", "")
        val header = """data class $name("""
        val footer = """)"""
        val all = (listOf(header) + listOf(fields) + listOf(footer)).joinToString("\n")
        return all
    }
    private fun field(name:String, typeQuantities:Map<String, Int>):String {
        val types = typeQuantities.keys
        val (nullTypes, nonNullTypes) = types.partition { it == "null" }
        val nullable = nullTypes.isNotEmpty()
        val simpleTypeName = if(nonNullTypes.isEmpty()) "Any" else if(nonNullTypes.size == 1) nonNullTypes.first() else "Any"
        val typeName = typeMap.getValue(simpleTypeName)
        val nullableTypeName = typeName + if(nullable) "?" else ""
        return "    val $name: $nullableTypeName"
    }
    companion object {
        val empty = TypeTracker(keyTypeQuantity = TwoKeyMap.empty())
        fun addType(typeTracker: TypeTracker, keyType:Pair<String, String>):TypeTracker {
            val (key, typeName) = keyType
            return typeTracker.addType(key, typeName)
        }
        private val typeMap = mapOf(
            "Integer" to "Int",
            "String" to "String",
            "LinkedHashMap" to "Map",
            "ArrayList" to "List"
        )
    }
}
