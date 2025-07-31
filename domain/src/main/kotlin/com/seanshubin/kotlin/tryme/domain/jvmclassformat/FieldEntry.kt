package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class FieldEntry(
    val accessFlags: Set<AccessFlag>,
    val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val descriptor: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val attributes: List<AttributeEntry>
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "accessFlags" to accessFlags.map { it.toString() },
            "name" to name.toObject(),
            "descriptor" to descriptor.toObject(),
            "attributes" to attributes.map { it.toObject() }
        )
    }
    companion object {
        fun fromFieldInfo(info: FieldInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): FieldEntry {
            val accessFlags = AccessFlag.fromMask(info.accessFlags.toInt())
            val name = constantPoolMap.getValue(info.nameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val descriptor = constantPoolMap.getValue(info.descriptorIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val attributes = info.attributes.map { AttributeEntry.fromAttributeInfo(it, constantPoolMap) }
            return FieldEntry(
                accessFlags = accessFlags,
                name = name,
                descriptor = descriptor,
                attributes = attributes
            )
        }
    }
}