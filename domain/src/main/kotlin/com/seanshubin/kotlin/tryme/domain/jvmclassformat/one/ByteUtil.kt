package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.nio.ByteBuffer
import java.nio.ByteOrder

object ByteUtil {
    fun longToBytes(value:Long): List<Byte> {
        return listOf(
            (value shr 56).toByte(),
            (value shr 48).toByte(),
            (value shr 40).toByte(),
            (value shr 32).toByte(),
            (value shr 24).toByte(),
            (value shr 16).toByte(),
            (value shr 8).toByte(),
            value.toByte()
        )
    }
    fun bytesToLong(bytes: List<Byte>): Long {
        require(bytes.size == 8) { "Byte array must be exactly 8 bytes long" }
        return (bytes[0].toLong() shl 56) or
               (bytes[1].toLong() shl 48) or
               (bytes[2].toLong() shl 40) or
               (bytes[3].toLong() shl 32) or
               (bytes[4].toLong() shl 24) or
               (bytes[5].toLong() shl 16) or
               (bytes[6].toLong() shl 8) or
               bytes[7].toLong()
    }
    fun intToBytes(value: Int): List<Byte> {
        return listOf(
            (value shr 24).toByte(),
            (value shr 16).toByte(),
            (value shr 8).toByte(),
            value.toByte()
        )
    }
    fun bytesToInt(bytes: List<Byte>): Int {
        require(bytes.size == 4) { "Byte array must be exactly 4 bytes long" }
        return (bytes[0].toInt() shl 24) or
                (bytes[1].toInt() shl 16) or
                (bytes[2].toInt() shl 8) or
                bytes[3].toInt()
    }
    fun shortToBytes(value: Short): List<Byte> {
        return listOf(
            (value.toInt() shr 8).toByte(),
            value.toByte()
        )
    }
    fun bytesToShort(bytes: List<Byte>): Short {
        require(bytes.size == 2) { "Byte array must be exactly 2 bytes long" }
        return ((bytes[0].toInt() shl 8) or bytes[1].toInt()).toShort()
    }
    fun doubleToBytes(value: Double): List<Byte> {
        val buffer = ByteBuffer.allocate(8)
        buffer.order(ByteOrder.BIG_ENDIAN)
        buffer.putDouble(value)
        return buffer.array().toList()
    }
    fun bytesToDouble(bytes: List<Byte>): Double {
        require(bytes.size == 8) { "Byte array must be exactly 8 bytes long" }
        val buffer = ByteBuffer.wrap(bytes.toByteArray())
        buffer.order(ByteOrder.BIG_ENDIAN)
        return buffer.getDouble()
    }
    fun floatToBytes(value: Float): List<Byte> {
        val buffer = ByteBuffer.allocate(4)
        buffer.order(ByteOrder.BIG_ENDIAN)
        buffer.putFloat(value)
        return buffer.array().toList()
    }
    fun bytesToFloat(bytes: List<Byte>): Float {
        require(bytes.size == 4) { "Byte array must be exactly 4 bytes long" }
        val buffer = ByteBuffer.wrap(bytes.toByteArray())
        buffer.order(ByteOrder.BIG_ENDIAN)
        return buffer.getFloat()
    }
}