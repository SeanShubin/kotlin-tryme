package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

enum class AccessFlag(val mask: Int) {
    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SUPER(0x0020),
    VOLATILE(0x0040),
    TRANSIENT(0x0080),
    INTERFACE(0x0200),
    ABSTRACT(0x0400),
    SYNTHETIC(0x1000),
    ANNOTATION(0x2000),
    ENUM(0x4000),
    MODULE(0x8000);

    companion object {
        fun fromDataInput(input: DataInput): Set<AccessFlag> {
            val mask = input.readUnsignedShort()
            return entries.filter { (mask and it.mask) != 0 }.toSet()
        }
    }
}
