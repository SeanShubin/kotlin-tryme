package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoNameAndType(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val nameIndex: UShort,
    val descriptorIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} nameIndex=$nameIndex descriptorIndex=$descriptorIndex"
    }
}
