package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoModule(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val nameIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String = "[$index] ${tag.line()} nameIndex=$nameIndex"
}
