package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

data class MethodInfo(
    val accessFlags: UShort,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "accessFlags" to accessFlags.toInt(),
            "nameIndex" to nameIndex.toInt(),
            "descriptorIndex" to descriptorIndex.toInt(),
            "attributesCount" to attributesCount.toInt(),
            "attributes" to attributes.map { it.toObject() }
        )
    }
    companion object {
        fun fromDataInput(input: DataInput): MethodInfo {
            val accessFlags = input.readUnsignedShort().toUShort()
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes = (0 until attributesCount.toInt()).map { AttributeInfo.fromDataInput(input) }
            return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
