package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

enum class ReferenceKind(val code: UByte) {
    GETFIELD(1u),
    GETSTATIC(2u),
    PUTFIELD(3u),
    PUTSTATIC(4u),
    INVOKEVIRTUAL(5u),
    INVOKESTATIC(6u),
    INVOKESPECIAL(7u),
    NEWINVOKESPECIAL(8u),
    INVOKEINTERFACE(9u);
fun line(): String = "${this.name}($code)"
    companion object {
        fun fromDataInput(input: DataInput): ReferenceKind {
            val code = input.readUnsignedByte().toUByte()
            return entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown reference kind code: $code")
        }
    }
}
