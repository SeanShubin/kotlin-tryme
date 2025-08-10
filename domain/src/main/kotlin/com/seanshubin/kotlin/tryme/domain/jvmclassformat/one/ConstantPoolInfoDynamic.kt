package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoDynamic(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} bootstrapMethodAttrIndex=$bootstrapMethodAttrIndex nameAndTypeIndex=$nameAndTypeIndex"
    }
}
