package com.seanshubin.kotlin.tryme.domain.atomic

data class Location(val x:Int, val y:Int) {
    fun moveX(deltaX: Int): Location {
        return Location(x + deltaX, y)
    }
    fun moveY(deltaY: Int): Location {
        return Location(x, y + deltaY)
    }
}