package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

interface FieldApi {
    val accessFlags: Set<AccessFlag>
    val name: ConstantPoolApi.Utf8Api
    val descriptor: ConstantPoolApi.Utf8Api
    val attributes: List<AttributeApi>
}
