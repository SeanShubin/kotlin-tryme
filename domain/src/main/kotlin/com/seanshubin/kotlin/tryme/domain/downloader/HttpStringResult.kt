package com.seanshubin.kotlin.tryme.domain.downloader

data class HttpStringResult(val statusCode:Int, val text:String){
    fun isOk():Boolean = statusCode in 200..299
    fun isNotOk():Boolean = !isOk()
}
