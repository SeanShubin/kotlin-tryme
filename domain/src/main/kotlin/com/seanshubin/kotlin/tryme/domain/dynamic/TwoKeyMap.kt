package com.seanshubin.kotlin.tryme.domain.dynamic

data class TwoKeyMap<Key1Type, Key2Type, ValueType>(val level1:Map<Key1Type, Map<Key2Type, ValueType>>) {
    fun put(key1: Key1Type, key2: Key2Type, value: ValueType):TwoKeyMap<Key1Type, Key2Type, ValueType> {
        val oldLevel2:Map<Key2Type, ValueType> = level1[key1] ?: emptyMap()
        val newLevel2 = oldLevel2 + Pair(key2, value)
        val newLevel1 = level1 + Pair(key1, newLevel2)
        return TwoKeyMap(newLevel1)
    }

    fun putIfMissing(key1: Key1Type, key2: Key2Type, value: ValueType):TwoKeyMap<Key1Type, Key2Type, ValueType> {
        return if(containsKey(key1, key2)) this
        else put(key1, key2, value)
    }

    fun containsKey(key1: Key1Type, key2: Key2Type): Boolean {
        val level2 = level1[key1] ?: return false
        return level2.containsKey(key2)
    }

    fun get(key1: Key1Type, key2: Key2Type): ValueType {
        val level2 = level1.getValue(key1)
        return level2.getValue(key2)
    }

    fun update(key1: Key1Type, key2: Key2Type, operation:(ValueType)->ValueType, default:ValueType):TwoKeyMap<Key1Type, Key2Type, ValueType> {
        val a = putIfMissing(key1, key2, default)
        val oldValue = a.get(key1, key2)
        val newValue = operation(oldValue)
        return put(key1, key2, newValue)
    }

    val keys:Set<Key1Type> get() = level1.keys

    companion object {
        val increment:(Int)->Int = { i: Int -> i + 1 }
        fun <Key1Type, Key2Type, ValueType> empty():TwoKeyMap<Key1Type, Key2Type, ValueType> = TwoKeyMap(emptyMap())
    }
}
