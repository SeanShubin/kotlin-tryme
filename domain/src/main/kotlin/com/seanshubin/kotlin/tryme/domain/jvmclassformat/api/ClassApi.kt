package com.seanshubin.kotlin.tryme.domain.jvmclassformat.api

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.AttributeInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.FieldInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.MethodInfo

interface ClassApi {
    val magic: UInt
    val minorVersion: UShort
    val majorVersion: UShort
    val constantPool: List<ConstantPoolApi>
    val accessFlags: Set<AccessFlag>
    val thisClass: ConstantPoolClassApi
    val superClass: ConstantPoolClassApi
    val interfaces: List<ConstantPoolClassApi>
    val fields: List<FieldApi>
    val methods: List<MethodApi>
    val attributes: List<AttributeApi>
}
