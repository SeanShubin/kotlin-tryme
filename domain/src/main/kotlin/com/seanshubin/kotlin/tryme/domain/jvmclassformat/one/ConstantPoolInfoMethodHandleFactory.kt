package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoMethodHandleFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoMethodHandle {
        val referenceKind = ReferenceKind.fromDataInput(input)
        val referenceIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoMethodHandle(index, tag, referenceKind, referenceIndex)
    }
}
