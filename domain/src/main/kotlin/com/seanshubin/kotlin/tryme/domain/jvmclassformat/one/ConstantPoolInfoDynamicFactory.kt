package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoDynamicFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoDynamic {
        val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
        val nameAndTypeIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoDynamic(index, tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
    }
}
