package com.seanshubin.kotlin.tryme.domain.string

object ByteArrayFormatHex : ByteArrayFormat {
    override fun encodeCompact(bytes: ByteArray): String {
        return bytes.joinToString("", transform = { it.toHex() })
    }

    override fun encodePretty(bytes: ByteArray): String {
        return bytes.map { it.toHex() }.windowed(16, 16, partialWindows = true)
            .joinToString("\n") { list -> list.joinToString(" ") }
    }

    override fun decode(s: String): ByteArray {
        return s.replace(whitespaceRegex, "").uppercase().toList().windowed(2, 2).map { (first, second) ->
            (digitValues.getValue(first) * 16 + digitValues.getValue(second)).toByte()
        }.toByteArray()
    }

    fun ByteArray.toLines():List<String> =
        toList().windowed(16, 16, partialWindows = true).map{it.toLine()}

    private fun List<Byte>.toLine():String {
        val hexes = this.map { it.toHex() }.joinToString(" ")
        val chars = this.map(::byteToDisplayChar).joinToString("")
        return "$hexes $chars"
    }

    fun Byte.toHex(): String {
        val asInt = toInt()
        val digits = "0123456789ABCDEF"
        val lowByte = asInt and 0b1111
        val highByte = asInt shr 4 and 0b1111
        val lowDigit = digits[lowByte]
        val highDigit = digits[highByte]
        return "" + highDigit + lowDigit
    }

    private fun byteToDisplayChar(byte: Byte): Char {
        if(byte < 32) return '.'
        else return byte.toInt().toChar()
    }

    private val digitValues = mapOf(
        '0' to 0,
        '1' to 1,
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'A' to 10,
        'B' to 11,
        'C' to 12,
        'D' to 13,
        'E' to 14,
        'F' to 15,
    )

    private val whitespaceRegex = Regex("""\s""")
}
