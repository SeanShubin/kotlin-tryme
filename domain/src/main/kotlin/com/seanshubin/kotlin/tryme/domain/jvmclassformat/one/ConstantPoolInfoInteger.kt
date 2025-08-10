package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToLine

class ConstantPoolInfoInteger(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val bytes: List<Byte>
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} bytes=${bytesToLine(bytes)}"
    }
}
