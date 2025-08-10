package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoDynamic(
    override val tag: ConstantPoolTag,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantPoolInfo
