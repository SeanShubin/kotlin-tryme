package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoMethodTypeFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoMethodType {
        val descriptorIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoMethodType(index, tag, descriptorIndex)
    }
}
