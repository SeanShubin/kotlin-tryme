package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

data class AttributeInfo(
    val attributeNameIndex: UShort,
    val attributeLength: Int,
    val info: List<Byte>
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "attributeNameIndex" to attributeNameIndex.toInt(),
            "attributeLength" to attributeLength,
            "info" to info
        )
    }

    companion object {
        fun fromDataInput(input: DataInput): AttributeInfo {
            val attributeNameIndex = input.readUnsignedShort().toUShort()
            val attributeLength = input.readInt()
            val info = ByteArray(attributeLength)
            input.readFully(info)
            return AttributeInfo(attributeNameIndex, attributeLength, info.toList())
        }
    }
}