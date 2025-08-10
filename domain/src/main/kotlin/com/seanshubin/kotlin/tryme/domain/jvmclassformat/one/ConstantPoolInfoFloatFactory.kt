package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoFloatFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index:Int, tag: ConstantPoolTag): ConstantPoolInfoFloat {
        val bytes = ByteArray(4)
        input.readFully(bytes)
        return ConstantPoolInfoFloat(index, tag, bytes.toList())
    }
}
