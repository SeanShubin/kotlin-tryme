package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.instructionset

sealed interface Instruction {
    val opCode: OpCode
    val bytes: List<Byte>

    data class NoArgs(
        override val opCode: OpCode,
        override val bytes: List<Byte>
    ) : Instruction
}