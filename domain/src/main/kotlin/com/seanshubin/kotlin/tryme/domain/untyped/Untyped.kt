package com.seanshubin.kotlin.tryme.domain.untyped

class Untyped(val value: Any?) {
    fun hasValueAtPath(vararg pathParts: Any?): Boolean =
        if (pathParts.isEmpty()) {
            true
        } else {
            if (valueIsMap()) {
                val mapValue = valueAsMap()
                val key = pathParts[0]
                if (mapValue.containsKey(key)) {
                    val innerValue = mapValue.getValue(key)
                    val remainingPathParts = pathParts.drop(1).toTypedArray()
                    Untyped(innerValue).hasValueAtPath(*remainingPathParts)
                } else {
                    false
                }
            } else {
                false
            }
        }

    fun getValueAtPath(vararg pathParts: Any?): Any? =
        if (pathParts.isEmpty()) {
            value
        } else {
            val mapValue = valueAsMap()
            val key = pathParts[0]
            val innerValue = mapValue.getValue(key)
            val remainingPathParts = pathParts.drop(1).toTypedArray()
            Untyped(innerValue).getValueAtPath(*remainingPathParts)
        }

    fun setValueAtPath(valueToSet:Any?, vararg pathParts:Any?):Untyped =
        if(pathParts.isEmpty()) {
            Untyped(valueToSet)
        } else {
            if(valueIsMap()){
                val key = pathParts[0]
                val mapValue = valueAsMap()
                val innerValue = if(mapValue.containsKey(key)){
                    mapValue.getValue(key)
                } else {
                    emptyMap<Any?, Any?>()
                }
                val remainingPathParts = pathParts.drop(1).toTypedArray()
                val newInnerUntyped = Untyped(innerValue).setValueAtPath(valueToSet, *remainingPathParts)
                val newInnerValue = newInnerUntyped.value
                val newPair = key to newInnerValue
                val newValue = mapValue + newPair
                Untyped(newValue)
            } else {
                Untyped(mapOf<Any?, Any?>()).setValueAtPath(valueToSet, *pathParts)
            }
        }

    private fun valueIsMap(): Boolean = value is Map<*, *>
    private fun valueAsMap(): Map<Any?, Any?> = value as Map<Any?, Any?>
}
