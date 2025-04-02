package com.seanshubin.kotlin.tryme.domain.dynamic

class SampleData(var index:Int = 0) {
    fun string():String = "string-${++index}"
    fun int():Int = ++index
    fun boolean():Boolean = (++index)%2==0
}
