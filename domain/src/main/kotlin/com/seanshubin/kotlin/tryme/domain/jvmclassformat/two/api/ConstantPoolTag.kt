package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

enum class ConstantPoolTag(val value: UByte) {
    Class(7u),
    Fieldref(9u),
    Methodref(10u),
    InterfaceMethodref(11u),
    String(8u),
    Integer(3u),
    Float(4u),
    Long(5u),
    Double(6u),
    NameAndType(12u),
    Utf8(1u),
    MethodHandle(15u),
    MethodType(16u),
    Dynamic(17u),
    InvokeDynamic(18u),
    Module(19u),
    Package(20u);
    companion object {
        fun fromValue(value: UByte): ConstantPoolTag {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unknown constant pool tag value: $value")
        }
    }
}
