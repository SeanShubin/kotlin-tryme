package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoMethodHandle(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val referenceKind: ReferenceKind,
    val referenceIndex: UShort
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} referenceKind=${referenceKind.line()} referenceIndex=$referenceIndex"
    }
}
