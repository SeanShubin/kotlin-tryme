package com.seanshubin.kotlin.tryme.domain.jvmclassformat

object FormatUtil {
    fun List<Byte>.toHex():List<String>{
        return this.map { byte ->
            val unsignedByte = byte.toInt() and 0xFF
            String.format("%02X", unsignedByte)
        }
    }
}