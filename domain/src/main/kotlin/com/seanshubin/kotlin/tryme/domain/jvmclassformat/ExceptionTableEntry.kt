package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

data class ExceptionTableEntry(
    val startPc: UShort,
    val endPc: UShort,
    val handlerPc: UShort,
    val catchType: ConstantPoolEntry.ConstantPoolEntryClass?
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "startPc" to startPc,
            "endPc" to endPc,
            "handlerPc" to handlerPc,
            "catchTypeIndex" to (catchType?.toObject() ?: "null")
        )
    }

    companion object {
        fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): ExceptionTableEntry {
            val startPc = input.readUnsignedShort().toUShort()
            val endPc = input.readUnsignedShort().toUShort()
            val handlerPc = input.readUnsignedShort().toUShort()
            val catchTypeIndex = input.readUnsignedShort()
            val catchType = if(catchTypeIndex == 0) null else constantPoolMap.getValue(catchTypeIndex) as ConstantPoolEntry.ConstantPoolEntryClass
            return ExceptionTableEntry(
                startPc = startPc,
                endPc = endPc,
                handlerPc = handlerPc,
                catchType = catchType
            )
        }
    }
}
