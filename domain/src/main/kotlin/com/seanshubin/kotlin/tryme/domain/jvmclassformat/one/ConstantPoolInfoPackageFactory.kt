package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoPackageFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoPackage {
        val nameIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoPackage(tag, nameIndex)
    }
}
