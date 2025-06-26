package com.seanshubin.kotlin.tryme.domain.dynamic

interface KeyValueStore {
    fun load(key:List<Any>):Any?
    fun store(key:List<Any>, value:Any?)
    fun exists(key:List<Any>):Boolean
    fun arraySize(key: List<Any>): Int
}
