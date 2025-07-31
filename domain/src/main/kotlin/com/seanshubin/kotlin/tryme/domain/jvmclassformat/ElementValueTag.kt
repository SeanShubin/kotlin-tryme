package com.seanshubin.kotlin.tryme.domain.jvmclassformat

enum class ElementValueTag(val charValue: Char) {
    BYTE('B'),
    CHAR('C'),
    DOUBLE('D'),
    FLOAT('F'),
    INT('I'),
    LONG('J'),
    SHORT('S'),
    BOOLEAN('Z'),
    STRING('s'),
    ENUM_CONSTANT('e'),
    CLASS('c'),
    ANNOTATION('@'),
    ARRAY('[');
    override fun toString():String = "$name($charValue)"
    companion object{
        fun fromByte(byte: Byte): ElementValueTag {
            val asChar = byte.toInt().toChar()
            return entries.firstOrNull { it.charValue == asChar }
                ?: throw IllegalArgumentException("Unknown tag byte: $byte")
        }
    }
}