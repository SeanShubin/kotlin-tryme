package com.seanshubin.kotlin.tryme.domain.dynamic

object ObjectKeyValueStore {
    fun getValue(o:Any?, path:List<Any?>):Any? {
        if(path.isEmpty()) return o
        if(o is Map<*,*>) {
            val subTreeKey = path.first()
            val subTree = o[subTreeKey]
            val remainingPath = path.drop(1)
            return getValue(subTree, remainingPath)
        } else {
            throw RuntimeException("No value at path ${path.joinToString(",")}")
        }
    }
    fun setValue(o:Any?, path:List<Any?>, value:Any?):Any? {
        if(path.isEmpty()) return value
        if(o is Map<*,*>) {
            val subTreeKey = path.first()
            val oldSubTree = o[subTreeKey]
            val remainingPath = path.drop(1)
            val newSubTree = setValue(oldSubTree, remainingPath, value)
            val entry = subTreeKey to newSubTree
            return o + entry
        } else {
            val subTreeKey = path.first()
            val oldSubTree = null
            val remainingPath = path.drop(1)
            val newSubTree = setValue(oldSubTree, remainingPath, value)
            return mapOf(subTreeKey to newSubTree)
        }

    }
    fun valueExists(o:Any?, path:List<Any?>):Boolean {
        if(path.isEmpty()) return true
        if(o is Map<*,*>) {
            val subTreeKey = path.first()
            val subTree = o[subTreeKey]
            val remainingPath = path.drop(1)
            return valueExists(subTree, remainingPath)
        } else {
            return false
        }
    }
}
