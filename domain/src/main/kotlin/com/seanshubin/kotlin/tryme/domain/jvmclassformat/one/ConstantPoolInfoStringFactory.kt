package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoStringFactory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoString {
        val stringIndex = input.readUnsignedShort().toUShort()
        return ConstantPoolInfoString(tag, stringIndex)
    }
}
