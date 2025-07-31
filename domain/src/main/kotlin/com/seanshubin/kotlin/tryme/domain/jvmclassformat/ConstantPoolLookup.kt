package com.seanshubin.kotlin.tryme.domain.jvmclassformat

interface ConstantPoolLookup {
    fun lookup(index: UShort): ConstantPoolInfo
}
