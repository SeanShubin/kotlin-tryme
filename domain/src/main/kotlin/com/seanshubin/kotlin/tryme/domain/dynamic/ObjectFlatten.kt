package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

object ObjectFlatten {
    fun flattenJsonObject(jsonString:String):String {
        val oldObject = JsonMappers.parser.readValue<Any?>(jsonString)
        val combineKey = {first:Any?, second:Any? -> "$first.$second"}
        val newObject = flattenObject(oldObject, combineKey)
        val newJsonString = JsonMappers.pretty.writeValueAsString(newObject)
        return newJsonString
    }

    fun flattenObject(o:Any?, combineKey:(Any?, Any?) -> Any?):Any?{
        if(o is Map<*,*>) return flattenedEntries(o, combineKey).toMap()
        else return o

    }

    private fun flattenedEntries(map:Map<*,*>, combineKey:(Any?, Any?) -> Any?):List<Pair<Any?, Any?>> {
        return map.flatMap{ (key, value) ->
            if(value is Map<*,*>){
                flattenedEntries(value, combineKey).map { (innerKey, innerValue) ->
                    Pair(combineKey(key, innerKey), innerValue)
                }
            } else {
                listOf(Pair(key, value))
            }
        }
    }
}
