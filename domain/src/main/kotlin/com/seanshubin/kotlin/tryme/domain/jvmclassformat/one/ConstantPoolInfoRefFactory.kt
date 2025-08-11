package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoRefFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoRef {
        val classIndex = input.readUnsignedShort().toUShort()
        val nameAndTypeIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoRef(index, tag, classIndex, nameAndTypeIndex)
    }
}
