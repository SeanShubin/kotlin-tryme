package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

data class ConstantPoolInfoRef(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val classIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} classIndex=$classIndex nameAndTypeIndex=$nameAndTypeIndex"
    }
}

