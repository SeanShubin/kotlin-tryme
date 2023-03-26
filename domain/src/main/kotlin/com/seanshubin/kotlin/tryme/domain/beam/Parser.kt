package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

interface Parser {
    fun parse(name:String, cursor: Cursor<Byte>):Result
}
