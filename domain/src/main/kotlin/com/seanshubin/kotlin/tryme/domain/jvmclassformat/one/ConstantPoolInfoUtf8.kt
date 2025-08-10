package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToLine

class ConstantPoolInfoUtf8(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val length: UShort,
    val bytes: List<Byte>
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} length=$length bytes=${bytesToLine(bytes)}"
    }
}
