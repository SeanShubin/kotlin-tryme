package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoPackageFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index:Int, tag: ConstantPoolTag): ConstantPoolInfoPackage {
        val nameIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoPackage(index,tag, nameIndex)
    }
}
