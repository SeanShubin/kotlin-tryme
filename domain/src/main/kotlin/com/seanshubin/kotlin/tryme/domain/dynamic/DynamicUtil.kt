package com.seanshubin.kotlin.tryme.domain.dynamic

object DynamicUtil {
    fun get(o: Any?, path: List<Any?>): Any? {
        if (path.isEmpty()) return o
        return if (o is Map<*, *>) {
            val key = path.first()
            val subPath = path.drop(1)
            val subTree = o[key]
            get(subTree, subPath)
        } else {
            throw RuntimeException("Path not found ${path.joinToString(".")}")
        }
    }

    fun set(o: Any?, path: List<Any?>, value: Any?): Any? {
        if (path.isEmpty()) return value
        return if (o is Map<*, *>) {
            val key = path.first()
            val oldSubTree = o[key]
            val subPath = path.drop(1)
            val newSubTree = set(oldSubTree, subPath, value)
            val entry = key to newSubTree
            o + entry
        } else {
            set(emptyMap<Any?, Any?>(), path, value)
        }

    }

    fun exists(o: Any?, path: List<Any?>): Boolean {
        if (path.isEmpty()) return true
        return if (o is Map<*, *>) {
            val key = path.first()
            val subPath = path.drop(1)
            val subTree = o[key]
            exists(subTree, subPath)
        } else {
            false
        }
    }

    fun flattenListAt(o: Any?, path: List<Any?>): List<Any?> {
        val theList = get(o, path) as List<Any?>
        return theList.map { element ->
            set(o, path, element)
        }
    }

    fun flattenListWithIndex(o: Any?, path: List<Any?>, valueKey: Any?, indexKey: Any?): List<Any?> {
        val theList = get(o, path) as List<Any?>
        return theList.mapIndexed { index, element ->
            val a = set(o, path + valueKey, element)
            val b = set(a, path + indexKey, index)
            b
        }
    }

    fun flattenMap(o: Any?, combineKey: (Any?, Any?) -> Any?): Any? {
        if (o !is Map<*, *>) return o
        return o.flatMap { (key, value) ->
            val flattenedInner = flattenMap(value, combineKey)
            if (flattenedInner is Map<*, *>) {
                flattenedInner.map{ (innerKey, innerValue) ->
                    val newKey = combineKey(key, innerKey)
                    newKey to innerValue
                }
            } else {
                listOf(key to flattenedInner)
            }
        }.toMap()
    }

    fun update(o: Any?, path: List<Any?>, default: Any?, operation: (Any?) -> Any?): Any? {
        val oldValue = if(exists(o, path)) get(o, path) else default
        val newValue = operation(oldValue)
        return set(o, path, newValue)
    }
}
