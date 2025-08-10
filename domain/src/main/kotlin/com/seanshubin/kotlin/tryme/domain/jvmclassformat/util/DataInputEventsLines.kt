package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToSanitizedString
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.sanitizeString

class DataInputEventsLines(val emit: (String) -> Unit) : LoggedDataInput.DataInputEvents {
    override fun readFully(b: ByteArray) {
        display("readFully", b)
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        display("readFully", b, off, len)
    }

    override fun skipBytes(n: Int, result: Int) {
        display("skipBytes", n, result)
    }

    override fun readBoolean(result: Boolean) {
        display("readBoolean", result)
    }

    override fun readByte(result: Byte) {
        display("readByte", result)
    }

    override fun readUnsignedByte(result: Int) {
        display("readUnsignedByte", result)
    }

    override fun readShort(result: Short) {
        display("readShort", result)
    }

    override fun readUnsignedShort(result: Int) {
        display("readUnsignedShort", result)
    }

    override fun readChar(result: Char) {
        display("readChar", result)
    }

    override fun readInt(result: Int) {
        display("readInt", result)
    }

    override fun readLong(result: Long) {
        display("readLong", result)
    }

    override fun readFloat(result: Float) {
        display("readFloat", result)
    }

    override fun readDouble(result: Double) {
        display("readDouble", result)
    }

    override fun readLine(result: String) {
        display("readLine", result)
    }

    override fun readUTF(result: String) {
        display("readUTF", result)
    }

    private fun display(caption: String, value: Int) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun display(caption: String, value: Long) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun display(caption: String, value: Double) {
        emit(String.format("%-17s: %10f %10H", caption, value, value))
    }

    private fun display(caption: String, value: Char) {
        emit(String.format("%-17s: %10s %10H", caption, value, value))
    }

    private fun display(caption: String, value: Byte) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun display(caption: String, value: Float) {
        emit(String.format("%-17s: %10f %10H", caption, value, value))
    }

    private fun display(caption: String, value: Short) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun display(caption: String, value: Boolean) {
        emit(String.format("%-17s: %10b %10H", caption, value, value))
    }

    private fun display(caption: String, value: String) {
        val asHex = bytesToHex(value.toByteArray(Charsets.UTF_8))
        val asString = sanitizeString(value)
        emit(String.format("%-17s: %10s %10s", caption, asString, asHex))
    }

    private fun display(caption: String, value: ByteArray) {
        val size = value.size
        val asHex = bytesToHex(value)
        val asString = bytesToSanitizedString(value)
        emit(String.format("%-17s: [%d] %s %s", caption, size, asString, asHex))
    }

    private fun display(caption: String, value: ByteArray, off: Int, len: Int) {
        val size = value.size
        val asHex = bytesToHex(value)
        val asString = bytesToSanitizedString(value)
        emit(String.format("%-17s: [%d] %s %s off=%d len=%d", caption, size, asString, asHex, off, len))
    }

    private fun display(caption: String, n: Int, result: Int) {
        emit(String.format("%-17s: n=%d result=%d", caption, n, result))
    }

}