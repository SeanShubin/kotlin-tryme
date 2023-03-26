package com.seanshubin.kotlin.tryme.domain.beam

object Display {
    fun Char.displayByte():String = this.code.displayByte()
    fun Byte.displayByte():String = this.toInt().displayByte()
    fun Int.displayByte():String =
        if(this < 32){
            "<byte $this>"
        } else {
            "<byte $this '${this.toChar()}'>"
        }
}
