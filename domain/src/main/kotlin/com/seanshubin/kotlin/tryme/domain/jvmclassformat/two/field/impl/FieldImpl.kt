package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.field.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AccessFlag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.FieldApi

data class FieldImpl(
    override val accessFlags: Set<AccessFlag>,
    override val name: String,
    override val descriptor: String,
    override val attributes: List<AttributeApi>
) : FieldApi {
    override fun lines(): List<String> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}