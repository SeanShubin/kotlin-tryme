package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

data class MethodInfo(
    val accessFlags: Set<AccessFlag>,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines():List<String>{
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
        fun fromDataInput(input: DataInput): MethodInfo {
            val accessFlags = AccessFlag.fromDataInput(input)
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val count = attributesCount.toInt()
            val attributes = (0 until count).map { AttributeInfo.fromDataInput(input) }
            return MethodInfo(
                accessFlags = accessFlags,
                nameIndex = nameIndex,
                descriptorIndex = descriptorIndex,
                attributesCount = attributesCount,
                attributes = attributes
            )
        }
    }
}
