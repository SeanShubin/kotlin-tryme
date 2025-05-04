package com.seanshubin.kotlin.tryme.domain.dynamic

interface KeyValueStoreWithDocumentation {
    fun load(key:List<String>, default:Any?, documentation:List<String>):Any?
}
