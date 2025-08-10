package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoNameAndType(
    override val tag: ConstantPoolTag,
    val nameIndex: UShort,
    val descriptorIndex: UShort
) : ConstantPoolInfo
