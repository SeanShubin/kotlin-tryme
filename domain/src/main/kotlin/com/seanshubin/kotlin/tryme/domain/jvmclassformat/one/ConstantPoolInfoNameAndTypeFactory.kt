package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoNameAndTypeFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoNameAndType {
        val nameIndex = input.readUnsignedShort().toUShort()
        val descriptorIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoNameAndType(tag, nameIndex, descriptorIndex)
    }
}
