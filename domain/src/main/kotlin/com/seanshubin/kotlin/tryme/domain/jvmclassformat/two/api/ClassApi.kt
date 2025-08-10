package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

interface ClassApi {
    val magic: UInt
    val minorVersion: UShort
    val majorVersion: UShort
    val constantPool: List<ConstantPoolApi>
    val accessFlags: Set<AccessFlag>
    val thisClass: ConstantPoolApi.ClassApi
    val superClass: ConstantPoolApi.ClassApi
    val interfaces: List<ConstantPoolApi.ClassApi>
    val fields: List<FieldApi>
    val methods: List<MethodApi>
    val attributes: List<AttributeApi>
}
