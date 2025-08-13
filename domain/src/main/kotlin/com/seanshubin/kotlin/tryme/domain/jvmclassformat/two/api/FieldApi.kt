package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AccessFlag

interface FieldApi {
    val accessFlags: Set<AccessFlag>
    val name: ConstantPoolApi.Utf8Api
    val descriptor: ConstantPoolApi.Utf8Api
    val attributes: List<AttributeApi>
    fun lines(): List<String>
}
