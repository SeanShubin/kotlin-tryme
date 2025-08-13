package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

class DebugDataInput(
    private val delegate: DataInput,
    private val emit: (String) -> Unit,
    private val profiler: Profiler
) : DataInput {
    override fun readFully(b: ByteArray) {
        profiler.measure("readFully") {
            delegate.readFully(b)
            debug("readFully", b)
        }
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun skipBytes(n: Int): Int {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readBoolean(): Boolean {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readByte(): Byte {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readUnsignedByte(): Int {
        return profiler.measure("readUnsignedByte") {
            val value = delegate.readUnsignedByte()
            debug("readUnsignedByte", value)
            value
        }
    }

    override fun readShort(): Short {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readUnsignedShort(): Int {
        return profiler.measure("readUnsignedShort") {
            val value = delegate.readUnsignedShort()
            debug("readUnsignedShort", value)
            value
        }
    }

    override fun readChar(): Char {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readInt(): Int {
        return profiler.measure("readInt") {
            val value = delegate.readInt()
            debug("readInt", value)
            value
        }
    }

    override fun readLong(): Long {
        return profiler.measure("readLong") {
            val value = delegate.readLong()
            debug("readLong", value)
            value
        }
    }

    override fun readFloat(): Float {
        return profiler.measure("readFloat") {
            val value = delegate.readFloat()
            debug("readFloat", value)
            value
        }
    }

    override fun readDouble(): Double {
        return profiler.measure("readDouble") {
            val value = delegate.readDouble()
            debug("readDouble", value)
            value
        }
    }

    override fun readLine(): String? {
        throw UnsupportedOperationException("Not Implemented!")
    }

    override fun readUTF(): String {
        throw UnsupportedOperationException("Not Implemented!")
    }

    private fun debug(caption: String, value: Int) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun debug(caption: String, value: Long) {
        emit(String.format("%-17s: %10d %10H", caption, value, value))
    }

    private fun debug(caption: String, value: Double) {
        emit(String.format("%-17s: %10f %10H", caption, value, value))
    }

    private fun debug(caption: String, value: Float) {
        emit(String.format("%-17s: %10f %10H", caption, value, value))
    }

    private fun debug(caption: String, value: ByteArray) {
        val size = value.size
        val hex = value.joinToString(" ") { byte ->
            String.format("%02X", byte)
        }
        val chars = value.joinToString("") { byte ->
            byte.toPrintableChar().toString()
        }
        emit(String.format("%-17s: [%d] %s %s", caption, size, chars, hex))
    }

    private fun Byte.toPrintableChar(): Char {
        return if (this in 32..126) {
            this.toInt().toChar()
        } else {
            '?'
        }
    }
}
