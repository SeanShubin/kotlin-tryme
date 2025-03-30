package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

object EmbeddedArrayFlatten {
    fun flattenJson(jsonString:String, path:List<String>, indexKey:String):List<Any?>{
        val o = JsonMappers.parser.readValue<Any?>(jsonString)
        return flatten(o, path, indexKey)
    }

    fun flatten(o:Any?, path:List<Any?>, indexKey:Any?):List<Any?>{
        return flattenAtPath(o, path, indexKey)
    }

    fun flattenAtPath(o:Any?, path:List<Any?>, indexKey:Any?):List<Any?> {
        val valueAtPath = ObjectKeyValueStore.getValue(o, path)
        if(valueAtPath is List<*>){
            return valueAtPath.mapIndexed { index, element ->
                val a = ObjectKeyValueStore.setValue(o, path, element)
                val b = ObjectKeyValueStore.setValue(a, path + indexKey, index)
                b
            }
        } else{
            throw  RuntimeException("List expected at path ${path.joinToString(",")}")
        }
    }
}
