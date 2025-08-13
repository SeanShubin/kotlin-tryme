package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

object DataFormat {
    fun formatBytes(caption: String, bytes: List<Byte>): String =
        formatBytes(caption, bytes.toByteArray())

    fun formatBytes(caption: String, bytes: ByteArray): String {
        val size = bytes.size
        val asHex = bytes.joinToString("") { String.format("%02X", it) }
        val asString = bytes.joinToString("") { if (it in 32..126) it.toInt().toChar().toString() else "." }
        return "${formatCaption(caption)}($size): $asString $asHex"
    }

    fun formatBoolean(caption: String, value: Boolean): String = "${formatCaption(caption)}: $value"
    fun formatByte(caption: String, value: Byte): String = "${formatCaption(caption)}: ${value.toInt()} (${value.toInt().toChar()})"
    fun formatInt(caption: String, value: Int): String = "${formatCaption(caption)}: $value (0x${value.toString(16)})"
    fun formatShort(caption: String, value: Short): String = "${formatCaption(caption)}: $value (0x${value.toString(16)})"
    fun formatChar(caption: String, value: Char): String = "${formatCaption(caption)}: ${value.code} (${value})"
    fun formatLong(caption: String, value: Long): String = "${formatCaption(caption)}: $value (0x${value.toString(16)})"
    fun formatFloat(caption: String, value: Float): String =
        "${formatCaption(caption)}: $value (0x${java.lang.Float.floatToIntBits(value).toString(16)})"

    fun formatDouble(caption: String, value: Double): String =
        "${formatCaption(caption)}: $value (0x${java.lang.Double.doubleToLongBits(value).toString(16)})"

    fun formatString(caption: String, value: String): String = formatBytes(caption, value.toByteArray().toList())
    private fun formatCaption(caption: String): String {
        return caption.padEnd(20, ' ')
    }
}
