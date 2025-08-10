package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoDouble(
    override val tag: ConstantPoolTag,
    val highBytes: List<Byte>,
    val lowBytes: List<Byte>
) : ConstantPoolInfo
