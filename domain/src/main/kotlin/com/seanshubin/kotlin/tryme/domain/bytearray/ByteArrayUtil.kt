package com.seanshubin.kotlin.tryme.domain.bytearray

object ByteArrayUtil {
  val digits = "0123456789ABCDEF"

  fun Byte.toHexString(): String {
    val higherDigitIndex = this.toInt() ushr 4 and 0b1111
    val lowerDigitIndex = this.toInt() and 0b1111
    val higherDigit = digits[higherDigitIndex]
    val lowerDigit = digits[lowerDigitIndex]
    return "$higherDigit$lowerDigit"
  }

  fun ByteArray.toHexString(): String = map { it.toHexString() }.joinToString("")
}