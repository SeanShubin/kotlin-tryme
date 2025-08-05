package com.seanshubin.kotlin.tryme.domain.jvmclassformat

object FormatUtil {
    fun List<Byte>.toHex():List<String>{
        return this.map { byte ->
            byte.toHex()
        }
    }

    fun Byte.toHex():String = String.format("%02X", this.toInt() and 0xFF)
}