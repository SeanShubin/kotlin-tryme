package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoStringFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfoString {
        val stringIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoString(index, tag, stringIndex)
    }
}
