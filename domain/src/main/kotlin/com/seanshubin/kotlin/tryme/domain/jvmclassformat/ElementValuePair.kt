package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

data class ElementValuePair(
    val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val value: ElementValueEntry
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "name" to name.toObject(),
            "value" to value.toObject()
        )
    }
    companion object {
        fun fromDataInput(dataInput: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): ElementValuePair {
            val elementNameIndex = dataInput.readShort()
            val elementNameEntry =
                constantPoolMap.getValue(elementNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val value = ElementValueEntry.fromDataInput(dataInput, constantPoolMap)
            return ElementValuePair(elementNameEntry, value)
        }
    }
}
