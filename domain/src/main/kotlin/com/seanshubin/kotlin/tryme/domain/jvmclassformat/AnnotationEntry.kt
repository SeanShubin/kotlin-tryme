package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

data class AnnotationEntry(
    val typeIndex:UShort,
    val fieldDescriptor: ConstantPoolEntry.ConstantPoolEntryUtf8,
    val numElementValuePairs:UShort,
    val elementValuePairs:List<ElementValuePair>
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "typeIndex" to typeIndex,
            "fieldDescriptor" to fieldDescriptor.toObject(),
            "numElementValuePairs" to numElementValuePairs,
            "elementValuePairs" to elementValuePairs.map { it.toObject() }
        )
    }
    companion object {
        fun fromDataInput(dataInput: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): AnnotationEntry {
            val typeIndex = dataInput.readUnsignedShort().toUShort()
            val fieldDescriptor = constantPoolMap.getValue(typeIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val numElementValuePairs = dataInput.readUnsignedShort().toUShort()
            val elementValuePairs = List(numElementValuePairs.toInt()) {
                ElementValuePair.fromDataInput(dataInput, constantPoolMap)
            }
            return AnnotationEntry(typeIndex, fieldDescriptor,numElementValuePairs, elementValuePairs)
        }
    }
}
