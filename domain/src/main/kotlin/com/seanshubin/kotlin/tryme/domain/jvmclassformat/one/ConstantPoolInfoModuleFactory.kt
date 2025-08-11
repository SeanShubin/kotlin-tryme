package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoModuleFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoModule {
        val nameIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoModule(index, tag, nameIndex)
    }
}
