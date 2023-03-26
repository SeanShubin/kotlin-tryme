package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

interface Expression {
    val name:String
    fun parse(start: Cursor<Byte>):Result
}
