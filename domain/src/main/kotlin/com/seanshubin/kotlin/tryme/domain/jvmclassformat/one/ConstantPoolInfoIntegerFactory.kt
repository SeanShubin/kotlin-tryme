package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoIntegerFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoInteger {
        val bytes = ByteArray(4)
        input.readFully(bytes)
        return ConstantPoolInfoInteger(index, tag, bytes.toList())
    }
}
