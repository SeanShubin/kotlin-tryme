package com.seanshubin.kotlin.tryme.domain.dynamic

object DynamicUtil {
    fun get(o: Any?, path: List<Any?>): Any? = Dynamic(o).get(path).o

    fun set(o: Any?, path: List<Any?>, value: Any?): Any? = Dynamic(o).set(path, value).o

    fun exists(o: Any?, path: List<Any?>): Boolean = Dynamic(o).exists(path)

    fun flattenListAt(o: Any?, path: List<Any?>): List<Any?> = Dynamic(o).flattenListAt(path).map { it.o }

    fun flattenListWithIndex(o: Any?, path: List<Any?>, valueKey: Any?, indexKey: Any?): List<Any?> = Dynamic(o).flattenListWithIndex(path, valueKey, indexKey).map { it.o }

    fun flattenMap(o: Any?, combineKey: (Any?, Any?) -> Any?): Any? = Dynamic(o).flattenMap(combineKey).o

    fun update(o: Any?, path: List<Any?>, default: Any?, operation: (Any?) -> Any?): Any? = Dynamic(o).update(path, default, operation).o
}
