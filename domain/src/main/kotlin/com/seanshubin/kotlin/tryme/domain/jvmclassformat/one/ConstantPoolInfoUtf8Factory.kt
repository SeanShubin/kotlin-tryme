package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoUtf8Factory : ConstantPoolInfoFactory {
    override fun fromDataInput(input: DataInput, tag: ConstantPoolTag): ConstantPoolInfoUtf8 {
        val length = input.readUnsignedShort()
        val bytes = ByteArray(length)
        input.readFully(bytes)
        return ConstantPoolInfoUtf8(tag, length.toUShort(), bytes.toList())
    }
}
