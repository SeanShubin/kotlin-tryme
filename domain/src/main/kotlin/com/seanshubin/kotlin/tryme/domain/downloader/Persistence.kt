package com.seanshubin.kotlin.tryme.domain.downloader

interface Persistence {
    fun addToSet(name:String, value:String)
    fun addToSet(name:String, values:List<String>)
}
