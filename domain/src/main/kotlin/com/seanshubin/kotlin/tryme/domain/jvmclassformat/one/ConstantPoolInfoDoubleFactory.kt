package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoDoubleFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoDouble {
        val highBytes = ByteArray(4)
        input.readFully(highBytes)
        val lowBytes = ByteArray(4)
        input.readFully(lowBytes)
        return ConstantPoolInfoDouble(tag, highBytes.toList(), lowBytes.toList())
    }
}
