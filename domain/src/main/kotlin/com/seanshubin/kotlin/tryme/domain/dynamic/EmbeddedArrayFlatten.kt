package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

object EmbeddedArrayFlatten {
    fun flattenJson(jsonString:String, path:List<String>, referenceKey:String, referenceValue:Any?, indexKey:String):List<Any?>{
        val o = JsonMappers.parser.readValue<Any?>(jsonString)
        return flatten(o, path, referenceKey, referenceValue, indexKey)
    }

    fun flatten(o:Any?, path:List<Any?>, referenceKey:Any?, referenceValue:Any?, indexKey:Any?):List<Any?>{
        return flattenAtPath(o, path, referenceKey, referenceValue, indexKey)
    }

    fun flattenAtPath(o:Any?, path:List<Any?>, referenceKey:Any?, referenceValue:Any?, indexKey:Any?):List<Any?> {
        val valueAtPath = ObjectKeyValueStore.getValue(o, path)
        if(valueAtPath is List<*>){
            return valueAtPath.mapIndexed { index, element ->
                val a = ObjectKeyValueStore.setValue(o, path, element)
                val b = ObjectKeyValueStore.setValue(a, path + referenceKey, referenceValue)
                val c = ObjectKeyValueStore.setValue(b, path + indexKey, index)
                c
            }
        } else{
            throw  RuntimeException("List expected at path ${path.joinToString(",")}")
        }
    }
}
