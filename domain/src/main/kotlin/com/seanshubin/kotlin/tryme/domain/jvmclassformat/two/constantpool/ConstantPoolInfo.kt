package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constantpool

sealed interface ConstantPoolInfo {
    data class Utf8Info(val tag:UByte, val length: UShort, val bytes:List<Byte>) : ConstantPoolInfo
}
