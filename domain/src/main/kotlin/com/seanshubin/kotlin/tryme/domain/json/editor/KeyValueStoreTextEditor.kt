package com.seanshubin.kotlin.tryme.domain.json.editor

interface KeyValueStoreTextEditor {
    fun <T> setValue(text:String, value:T, vararg keys:String):String
    fun <T> getValue(converter:Converter<T>, text:String, vararg keys:String):T
    fun valueExists(text:String, vararg keys:String):Boolean
}