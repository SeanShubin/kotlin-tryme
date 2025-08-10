package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

class ConstantPoolInfoMethodHandle(
    override val tag: ConstantPoolTag,
    val referenceKind: ReferenceKind,
    val referenceIndex: UShort
) : ConstantPoolInfo
