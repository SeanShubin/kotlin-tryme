package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

data class ConstantPoolInfoRef(
    override val tag: ConstantPoolTag,
    val classIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantPoolInfo
