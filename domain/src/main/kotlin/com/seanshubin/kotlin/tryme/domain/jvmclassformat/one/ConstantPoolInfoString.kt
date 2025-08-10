package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoString(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val stringIndex: UShort
) : ConstantPoolInfo {
    override fun line(): String = "[$index] ${tag.line()} stringIndex=$stringIndex"
}
