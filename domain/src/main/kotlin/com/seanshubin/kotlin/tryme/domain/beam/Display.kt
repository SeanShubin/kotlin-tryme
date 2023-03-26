package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.string.ByteArrayFormatHex.toHex

object Display {
    fun Char.displayByte():String = this.code.displayByte()
    fun Byte.displayByte():String = this.toInt().displayByte()
    fun Int.displayByte():String =
        if(this < 32){
            "<byte $this \\x${this.toByte().toHex()}>"
        } else {
            "<byte $this \\x${this.toByte().toHex()} '${this.toChar()}'>"
        }
}
