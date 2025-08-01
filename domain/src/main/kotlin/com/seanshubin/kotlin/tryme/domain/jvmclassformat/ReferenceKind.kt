package com.seanshubin.kotlin.tryme.domain.jvmclassformat

enum class ReferenceKind(val code: UByte) {
    GetField(1u),
    GetStatic(2u),
    PutField(3u),
    PutStatic(4u),
    InvokeVirtual(5u),
    InvokeStatic(6u),
    InvokeSpecial(7u),
    NewInvokeSpecial(8u),
    InvokeInterface(9u);

    override fun toString(): String = "$name($code)"

    companion object {
        fun fromCode(code: UByte): ReferenceKind {
            val value = entries.find { it.code == code }
            if (value == null) throw RuntimeException("No ReferenceKind with code $code")
            return value
        }
    }
}
