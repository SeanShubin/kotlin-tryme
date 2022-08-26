package com.seanshubin.kotlin.tryme.domain.json.editor

interface KeyValueStore {
    val text:String
    fun <T> setValue(value:T, vararg keys:String):String
    fun <T> getValue(converter:Converter<T>, vararg keys:String):T
    fun valueExists(vararg keys:String):Boolean
}
