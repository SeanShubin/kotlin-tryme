package com.seanshubin.kotlin.tryme.domain.jvmclassformat

object ByteUtil {
    fun bytesToInt(bytes: List<Byte>): Int {
        require(bytes.size == 4) { "Byte list must contain exactly 4 bytes." }
        return (bytes[0].toInt() shl 24) or
               (bytes[1].toInt() and 0xFF shl 16) or
               (bytes[2].toInt() and 0xFF shl 8) or
               (bytes[3].toInt() and 0xFF)
    }

    fun bytesToUShort(bytes: List<Byte>): UShort {
        require(bytes.size == 2) { "Byte list must contain exactly 2 bytes." }
        return ((bytes[0].toInt() and 0xFF) shl 8 or (bytes[1].toInt() and 0xFF)).toUShort()
    }
}
