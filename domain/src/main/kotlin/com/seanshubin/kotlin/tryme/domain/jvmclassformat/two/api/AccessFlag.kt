package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

enum class AccessFlag(val mask: UShort) {
    PUBLIC(0x0001u),
    PRIVATE(0x0002u),
    PROTECTED(0x0004u),
    STATIC(0x0008u),
    FINAL(0x0010u),
    SUPER(0x0020u),
    VOLATILE(0x0040u),
    TRANSIENT(0x0080u),
    INTERFACE(0x0200u),
    ABSTRACT(0x0400u),
    SYNTHETIC(0x1000u),
    ANNOTATION(0x2000u),
    ENUM(0x4000u),
    MODULE(0x8000u);

    companion object {
        val USHORT_ZERO:UShort = 0u
        fun fromMask(mask: UShort): Set<AccessFlag> {
            val entries = entries.filter { (mask and it.mask) != USHORT_ZERO }
            return entries.toSet()
        }

        fun toMask(flags: Set<AccessFlag>): UShort {
            return flags.fold(USHORT_ZERO) { acc, flag -> acc or flag.mask }
        }
    }
}
