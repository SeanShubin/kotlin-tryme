package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoDynamicFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoDynamic {
        val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
        val nameAndTypeIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoDynamic(tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
    }
}
