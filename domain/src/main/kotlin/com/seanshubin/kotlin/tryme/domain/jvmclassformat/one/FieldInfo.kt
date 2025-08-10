package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

data class FieldInfo(
    val accessFlags: UShort,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(): List<String> {
        return listOf(
            "accessFlags: $accessFlags",
            "nameIndex: $nameIndex",
            "descriptorIndex: $descriptorIndex",
            "attributesCount: $attributesCount"
        ) + attributes.mapIndexed { index, attributeInfo ->
            "attribute[$index]:" + attributeInfo.line()
        }
    }
    companion object {
        fun fromDataInput(input: DataInput): FieldInfo {
            val accessFlags = input.readUnsignedShort().toUShort()
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes = (0 until attributesCount.toInt()).map { AttributeInfo.fromDataInput(input) }
            return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
