package com.seanshubin.kotlin.tryme.domain.jvmclassformat

enum class PrimitiveArrayType(val code: Byte) {
    BOOLEAN(4),
    CHAR(5),
    FLOAT(6),
    DOUBLE(7),
    BYTE(8),
    SHORT(9),
    INT(10),
    LONG(11);

    companion object {
        fun fromCode(code: Byte): PrimitiveArrayType {
            return entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown array type code: $code")
        }
    }
}