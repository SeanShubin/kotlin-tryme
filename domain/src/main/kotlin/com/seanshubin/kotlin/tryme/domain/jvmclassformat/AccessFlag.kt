package com.seanshubin.kotlin.tryme.domain.jvmclassformat

enum class AccessFlag(val mask:Int) {
    ACC_PUBLIC(0x0001),
    ACC_PRIVATE(0x0002),
    ACC_PROTECTED(0x0004),
    ACC_STATIC(0x0008),
    ACC_FINAL(0x0010),
    ACC_SUPER(0x0020),
    ACC_VOLATILE(0x0040),
    ACC_TRANSIENT(0x0080),
    ACC_INTERFACE(0x0200),
    ACC_ABSTRACT(0x0400),
    ACC_SYNTHETIC(0x1000),
    ACC_ANNOTATION(0x2000),
    ACC_ENUM(0x4000),
    ACC_MODULE(0x8000);
    companion object {
        fun fromMask(mask: Int): Set<AccessFlag> {
            return entries.filter { (mask and it.mask) != 0 }.toSet()
        }

        fun toMask(flags: Set<AccessFlag>): Int {
            return flags.fold(0) { acc, flag -> acc or flag.mask }
        }
    }
}
