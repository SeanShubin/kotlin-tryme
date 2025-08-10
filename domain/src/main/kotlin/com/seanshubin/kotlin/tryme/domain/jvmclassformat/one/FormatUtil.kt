package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

object FormatUtil {
    fun bytesToLine(bytes:List<Byte>):String {
        val compactHex = bytes.joinToString(""){byteToHex(it)}
        val sanitizedString = String(bytes.map(::sanitizeByte).toByteArray(), Charsets.UTF_8)
        return "$compactHex $sanitizedString"
    }
    fun byteToHex(byte: Byte): String {
        return String.format("%02x", byte)
    }
    fun sanitizeByte(byte: Byte): Byte {
        return if (byte in 0x20.toByte()..0x7E.toByte()) {
            byte
        } else {
            '.'.code.toByte()
        }
    }
//    fun toqHexString(byte: Byte): String {
//        return String.format("%02x", byte)
//    }
//    fun toqHexString(bytes: List<Byte>): String {
//        return bytes.joinToString("") { bytesToLine(it) }
//    }
}