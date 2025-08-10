package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoMethodTypeFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoMethodType {
        val descriptorIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoMethodType(tag, descriptorIndex)
    }
}
