package com.seanshubin.kotlin.tryme.domain.config

class Untyped(val value: Any?) {
    fun asInt(): Int = value as Int
    fun asString(): String = value as String
    fun valueExistsAtPath(vararg pathParts: Any?): Boolean {
        return if (pathParts.isEmpty()) {
            true
        } else {
            val key = pathParts[0]
            if (value !is Map<*, *>) return false
            value as Map<Any?, Any?>
            if (value.containsKey(key)) {
                val inner = Untyped(value.getValue(key))
                val pathRemain = pathParts.drop(1).toTypedArray()
                inner.valueExistsAtPath(*pathRemain)
            } else {
                false
            }
        }
    }

    fun getValueAtPath(vararg pathParts: Any?): Untyped {
        return if (pathParts.isEmpty()) {
            this
        } else {
            value as Map<Any?, Any?>
            val inner = Untyped(value.getValue(pathParts[0]))
            val pathRemain = pathParts.drop(1).toTypedArray()
            inner.getValueAtPath(*pathRemain)
        }
    }

    fun setValueAtPath(valueToSet: Any?, vararg pathParts: Any?): Untyped {
        return if (pathParts.isEmpty()) {
            Untyped(valueToSet)
        } else {
            val key = pathParts[0]
            val currentAsMap = if (value is Map<*, *>) {
                value as Map<Any?, Any?>
            } else {
                emptyMap<Any?, Any?>()
            }
            val inner = if (currentAsMap.containsKey(key)) {
                Untyped(currentAsMap.getValue(key))
            } else {
                Untyped(emptyMap<Any?, Any?>())
            }
            val pathRemain = pathParts.drop(1).toTypedArray()
            val newInner = inner.setValueAtPath(valueToSet, *pathRemain)
            val newValue = currentAsMap + (key to newInner.value)
            Untyped(newValue)
        }
    }
}