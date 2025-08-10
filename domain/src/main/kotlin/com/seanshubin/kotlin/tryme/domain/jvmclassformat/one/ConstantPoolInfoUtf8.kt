package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoUtf8(
    override val tag: ConstantPoolTag,
    val length: UShort,
    val bytes: List<Byte>
) : ConstantPoolInfo
