package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoRefFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoRef {
        val classIndex = input.readUnsignedShort().toUShort()
        val nameAndTypeIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoRef(tag, classIndex, nameAndTypeIndex)
    }
}
