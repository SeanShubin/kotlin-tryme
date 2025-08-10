package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

object FormatUtil {
    val indent:(String)->String = { "  $it" }
    fun bytesToDisplay(bytes: List<Byte>): String {
        val asHex = bytesToHex(bytes)
        val asString = bytesToSanitizedString(bytes)
        return "$asHex $asString"
    }

    fun bytesToDisplay(bytes: ByteArray): String {
        val asHex = bytesToHex(bytes)
        val asString = bytesToSanitizedString(bytes)
        return "$asHex $asString"
    }

    fun sanitizeString(s:String):String {
        return s.map { sanitizeByte(it.code.toByte()) }.map { it.toInt().toChar() }.joinToString("")
    }

    fun bytesToSanitizedString(bytes: List<Byte>): String {
        return String(bytes.map(::sanitizeByte).toByteArray(), Charsets.UTF_8)
    }

    fun bytesToSanitizedString(bytes: ByteArray): String {
        return String(bytes.map(::sanitizeByte).toByteArray(), Charsets.UTF_8)
    }

    fun bytesToHex(bytes: List<Byte>): String {
        return bytes.joinToString("") { byteToHex(it) }
    }

    fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { byteToHex(it) }
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
}
