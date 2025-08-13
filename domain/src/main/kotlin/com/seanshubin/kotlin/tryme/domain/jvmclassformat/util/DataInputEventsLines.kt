package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToSanitizedString
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.sanitizeString

class DataInputEventsLines(val emit: (String) -> Unit) : LoggedDataInput.DataInputEvents {
    override fun readFully(b: ByteArray) {
        emit(DataFormat.formatBytes("readFully", b))
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        emit(DataFormat.formatBytes("readFully", b.sliceArray(off until off + len)))
    }

    override fun skipBytes(n: Int, result: Int) {
        emit("skipBytes($n, $result)")
    }

    override fun readBoolean(result: Boolean) {
        emit(DataFormat.formatBoolean("readBoolean", result))
    }

    override fun readByte(result: Byte) {
        emit(DataFormat.formatByte("readByte", result))
    }

    override fun readUnsignedByte(result: Int) {
        emit(DataFormat.formatInt("readUnsignedByte", result))
    }

    override fun readShort(result: Short) {
        emit(DataFormat.formatShort("readShort", result))
    }

    override fun readUnsignedShort(result: Int) {
        emit(DataFormat.formatInt("readUnsignedShort", result))
    }

    override fun readChar(result: Char) {
        emit(DataFormat.formatChar("readChar", result))
    }

    override fun readInt(result: Int) {
        emit(DataFormat.formatInt("readInt", result))
    }

    override fun readLong(result: Long) {
        emit(DataFormat.formatLong("readLong", result))
    }

    override fun readFloat(result: Float) {
        emit(DataFormat.formatFloat("readFloat", result))
    }

    override fun readDouble(result: Double) {
        emit(DataFormat.formatDouble("readDouble", result))
    }

    override fun readLine(result: String) {
        emit(DataFormat.formatString("readLine", result))
    }

    override fun readUTF(result: String) {
        emit(DataFormat.formatString("readUTF", result))
    }
}
