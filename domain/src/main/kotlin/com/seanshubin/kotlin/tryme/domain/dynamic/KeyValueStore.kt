package com.seanshubin.kotlin.tryme.domain.dynamic

interface KeyValueStore {
    fun load(key:List<String>):Any?
    fun store(key:List<String>, value:Any?)
    fun exists(key:List<String>):Boolean
}
