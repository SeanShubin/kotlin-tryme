package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class CodeBlock(val opCodes: List<OpCodeEntry>) {
    fun toObject(): List<Any> {
        return opCodes.map { it.toObject() }
    }
}
