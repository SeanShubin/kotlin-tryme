package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream

class LoggedDataInput(private val delegate: DataInput, private val dataInputEvents: DataInputEvents) : DataInput {
    override fun readFully(b: ByteArray) {
        delegate.readFully(b)
        dataInputEvents.readFully(b)
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        delegate.readFully(b, off, len)
        dataInputEvents.readFully(b, off, len)
    }

    override fun skipBytes(n: Int): Int {
        val result = delegate.skipBytes(n)
        dataInputEvents.skipBytes(n, result)
        return result
    }

    override fun readBoolean(): Boolean {
        val result = delegate.readBoolean()
        dataInputEvents.readBoolean(result)
        return result
    }

    override fun readByte(): Byte {
        val result = delegate.readByte()
        dataInputEvents.readByte(result)
        return result
    }

    override fun readUnsignedByte(): Int {
        val result = delegate.readUnsignedByte()
        dataInputEvents.readUnsignedByte(result)
        return result
    }

    override fun readShort(): Short {
        val result = delegate.readShort()
        dataInputEvents.readShort(result)
        return result
    }

    override fun readUnsignedShort(): Int {
        val result = delegate.readUnsignedShort()
        dataInputEvents.readUnsignedShort(result)
        return result
    }

    override fun readChar(): Char {
        val result = delegate.readChar()
        dataInputEvents.readChar(result)
        return result
    }

    override fun readInt(): Int {
        val result = delegate.readInt()
        dataInputEvents.readInt(result)
        return result
    }

    override fun readLong(): Long {
        val result = delegate.readLong()
        dataInputEvents.readLong(result)
        return result
    }

    override fun readFloat(): Float {
        val result = delegate.readFloat()
        dataInputEvents.readFloat(result)
        return result
    }

    override fun readDouble(): Double {
        val result = delegate.readDouble()
        dataInputEvents.readDouble(result)
        return result
    }

    override fun readLine(): String? {
        val result = delegate.readLine()
        dataInputEvents.readLine(result ?: "")
        return result
    }

    override fun readUTF(): String {
        val result = delegate.readUTF()
        dataInputEvents.readUTF(result)
        return result
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, events:DataInputEvents):LoggedDataInput{
            val dataInputStream = DataInputStream(inputStream)
            return LoggedDataInput(dataInputStream, events)
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
