package com.seanshubin.kotlin.tryme.domain.jvmclassformat.constantpool

enum class ConstantPoolTag(val tagName: String, val tagId: UByte, val entriesTaken: Int) {
    UTF8("Utf8", 1u, 1),
    INTEGER("Integer", 3u, 1),
    FLOAT("Float", 4u, 1),
    LONG("Long", 5u, 2),
    DOUBLE("Double", 6u, 2),
    CLASS("Class", 7u, 1),
    STRING("String", 8u, 1),
    FIELDREF("Fieldref", 9u, 1),
    METHODREF("Methodref", 10u, 1),
    INTERFACEMETHODREF("InterfaceMethodref", 11u, 1),
    NAMEANDTYPE("NameAndType", 12u, 1),
    METHODHANDLE("MethodHandle", 15u, 1),
    METHODTYPE("MethodType", 16u, 1),
    DYNAMIC("Dynamic", 17u, 1),
    INVOKEDYNAMIC("InvokeDynamic", 18u, 1),
    MODULE("Module", 19u, 1),
    PACKAGE("Package", 20u, 1);
}
