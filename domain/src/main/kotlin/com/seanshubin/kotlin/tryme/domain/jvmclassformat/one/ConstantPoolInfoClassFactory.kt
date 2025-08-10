package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoClassFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoClass {
        val nameIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoClass(tag, nameIndex)
    }
}
