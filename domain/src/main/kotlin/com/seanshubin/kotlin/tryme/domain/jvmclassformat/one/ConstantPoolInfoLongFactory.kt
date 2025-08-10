package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoLongFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index:Int, tag: ConstantPoolTag): ConstantPoolInfoLong {
        val highBytes = ByteArray(4)
        input.readFully(highBytes)
        val lowBytes = ByteArray(4)
        input.readFully(lowBytes)
        return ConstantPoolInfoLong(index, tag, highBytes.toList(), lowBytes.toList())
    }
}
