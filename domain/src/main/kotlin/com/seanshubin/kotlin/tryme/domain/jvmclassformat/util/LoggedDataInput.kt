package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream

class LoggedDataInput(
    private val delegate: DataInput,
    private val dataInputEvents: DataInputEvents,
    private val profiler:Profiler
) : DataInput {
    override fun readFully(b: ByteArray) {
        profiler.measure("readFully") {
            delegate.readFully(b)
            dataInputEvents.readFully(b)
        }
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        profiler.measure("readFully") {
            delegate.readFully(b, off, len)
            dataInputEvents.readFully(b, off, len)
        }
    }

    override fun skipBytes(n: Int): Int {
        return profiler.measure("skipBytes") {
            val result = delegate.skipBytes(n)
            dataInputEvents.skipBytes(n, result)
            result
        }
    }

    override fun readBoolean(): Boolean {
        return profiler.measure("readBoolean") {
            val result = delegate.readBoolean()
            dataInputEvents.readBoolean(result)
            result
        }
    }

    override fun readByte(): Byte {
        return profiler.measure("readByte") {
            val result = delegate.readByte()
            dataInputEvents.readByte(result)
            result
        }
    }

    override fun readUnsignedByte(): Int {
        return profiler.measure("readUnsignedByte") {
            val result = delegate.readUnsignedByte()
            dataInputEvents.readUnsignedByte(result)
            result
        }
    }

    override fun readShort(): Short {
        return profiler.measure("readShort") {
            val result = delegate.readShort()
            dataInputEvents.readShort(result)
            result
        }
    }

    override fun readUnsignedShort(): Int {
        return profiler.measure("readUnsignedShort") {
            val result = delegate.readUnsignedShort()
            dataInputEvents.readUnsignedShort(result)
            result
        }
    }

    override fun readChar(): Char {
        return profiler.measure("readChar") {
            val result = delegate.readChar()
            dataInputEvents.readChar(result)
            result
        }
    }

    override fun readInt(): Int {
        return profiler.measure("readInt") {
            val result = delegate.readInt()
            dataInputEvents.readInt(result)
            result
        }
    }

    override fun readLong(): Long {
        return profiler.measure("readLong") {
            val result = delegate.readLong()
            dataInputEvents.readLong(result)
            result
        }
    }

    override fun readFloat(): Float {
        return profiler.measure("readFloat") {
            val result = delegate.readFloat()
            dataInputEvents.readFloat(result)
            result
        }
    }

    override fun readDouble(): Double {
        return profiler.measure("readDouble") {
            val result = delegate.readDouble()
            dataInputEvents.readDouble(result)
            result
        }
    }

    override fun readLine(): String? {
        return profiler.measure("readLine") {
            val result = delegate.readLine()
            dataInputEvents.readLine(result ?: "")
            result
        }
    }

    override fun readUTF(): String {
        return profiler.measure("readUTF") {
            val result = delegate.readUTF()
            dataInputEvents.readUTF(result)
            result
        }
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, events:DataInputEvents, profiler:Profiler):LoggedDataInput{
            val dataInputStream = DataInputStream(inputStream)
            return LoggedDataInput(dataInputStream, events, profiler)
        }
    }

    interface DataInputEvents {
        fun readFully(b: ByteArray)
        fun readFully(b: ByteArray, off: Int, len: Int)
        fun skipBytes(n: Int, result: Int)
        fun readBoolean(result: Boolean)
        fun readByte(result: Byte)
        fun readUnsignedByte(result: Int)
        fun readShort(result: Short)
        fun readUnsignedShort(result: Int)
        fun readChar(result: Char)
        fun readInt(result: Int)
        fun readLong(result: Long)
        fun readFloat(result: Float)
        fun readDouble(result: Double)
        fun readLine(result: String)
        fun readUTF(result: String)

        companion object {
            val nop = object : DataInputEvents {
                override fun readFully(b: ByteArray) {}
                override fun readFully(b: ByteArray, off: Int, len: Int) {}
                override fun skipBytes(n: Int, result: Int) {}
                override fun readBoolean(result: Boolean) {}
                override fun readByte(result: Byte) {}
                override fun readUnsignedByte(result: Int) {}
                override fun readShort(result: Short) {}
                override fun readUnsignedShort(result: Int) {}
                override fun readChar(result: Char) {}
                override fun readInt(result: Int) {}
                override fun readLong(result: Long) {}
                override fun readFloat(result: Float) {}
                override fun readDouble(result: Double) {}
                override fun readLine(result: String) {}
                override fun readUTF(result: String) {}
            }
        }
    }
}
