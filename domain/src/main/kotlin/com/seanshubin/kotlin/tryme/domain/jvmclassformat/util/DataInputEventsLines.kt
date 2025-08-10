package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.doubleToBytes
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.intToBytes
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.longToBytes
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.shortToBytes
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToDisplay
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.sanitizeString

class DataInputEventsLines(val emit:(String)->Unit): LoggedDataInput.DataInputEvents {
    override fun readFully(b: ByteArray) {
        emit("readFully(size=${b.size}, bytes=0x${bytesToDisplay(b)})")
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        emit("readFully(off=$off, len=$len, size=${b.size}, bytes=0x${bytesToDisplay(b)})")
    }

    override fun skipBytes(n: Int, result: Int) {
        emit("skipBytes($n, $result)")
    }

    override fun readBoolean(result: Boolean) {
        emit("readBoolean($result)")
    }

    override fun readByte(result: Byte) {
        emit("readByte($result 0x${bytesToHex(listOf(result))})")
    }

    override fun readUnsignedByte(result: Int) {
        emit("readUnsignedByte($result 0x${bytesToHex(listOf(result.toByte()))})")
    }

    override fun readShort(result: Short) {
        emit("readShort($result 0x${bytesToHex(shortToBytes(result))})")
    }

    override fun readUnsignedShort(result: Int) {
        emit("readUnsignedShort($result 0x${bytesToHex(shortToBytes(result.toShort()))})")
    }

    override fun readChar(result: Char) {
        emit("readChar($result 0x${bytesToHex(listOf(result.code.toByte()))})")
    }

    override fun readInt(result: Int) {
        emit("readInt($result 0x${bytesToHex(intToBytes(result))})")
    }

    override fun readLong(result: Long) {
        emit("readLong($result 0x${bytesToHex(longToBytes(result))})")
    }

    override fun readFloat(result: Float) {
        emit("readFloat($result 0x${bytesToHex(doubleToBytes(result.toDouble()))})")
    }

    override fun readDouble(result: Double) {
        emit("readDouble($result 0x${bytesToHex(doubleToBytes(result))})")
    }

    override fun readLine(result: String) {
        emit("readLine(${result.length} '${sanitizeString(result)}')")
    }

    override fun readUTF(result: String) {
        emit("readUTF(${result.length} '${sanitizeString(result)}')")
    }
}