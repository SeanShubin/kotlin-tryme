package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

enum class ConstantPoolTag(val value: UByte, val entriesTaken: Int) {
    CLASS(7u, 1),
    FIELDREF(9u, 1),
    METHODREF(10u, 1),
    INTERFACEMETHODREF(11u, 1),
    STRING(8u, 1),
    INTEGER(3u, 1),
    FLOAT(4u, 1),
    LONG(5u, 2),
    DOUBLE(6u, 2),
    NAMEANDTYPE(12u, 1),
    UTF8(1u, 1),
    METHODHANDLE(15u, 1),
    METHODTYPE(16u, 1),
    DYNAMIC(17u, 1),
    INVOKEDYNAMIC(18u, 1),
    MODULE(19u, 1),
    PACKAGE(20u, 1);

    companion object {
        fun fromDataInput(input: DataInput): ConstantPoolTag {
            val value = input.readUnsignedByte().toUByte()
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unknown constant pool tag value: $value")
        }
    }
}
