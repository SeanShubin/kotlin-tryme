package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class MethodEntry(
    val accessFlags: Set<AccessFlag>,
    val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val descriptor: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val attributes: List<AttributeEntry>,
    val codeAttribute: AttributeEntry.CodeEntry?
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
        fun fromMethodInfo(info: MethodInfo, constantPoolMap: Map<Int, ConstantPoolEntry>, events:Events): MethodEntry {
            val accessFlags = AccessFlag.fromMask(info.accessFlags.toInt())
            val name = constantPoolMap.getValue(info.nameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val descriptor =
                constantPoolMap.getValue(info.descriptorIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val attributes = info.attributes.map { AttributeEntry.fromAttributeInfo(it, constantPoolMap, events) }
            val codeAttribute = attributes.filterIsInstance<AttributeEntry.CodeEntry>().singleOrNull()
            return MethodEntry(
                accessFlags = accessFlags,
                name = name,
                descriptor = descriptor,
                attributes = attributes,
                codeAttribute = codeAttribute
            )
        }
    }
}