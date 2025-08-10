package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoMethodType(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val descriptorIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} descriptorIndex=$descriptorIndex"
    }
}
