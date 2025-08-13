package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.method.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AccessFlag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.FieldApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.MethodApi

data class MethodImpl(
    override val accessFlags: Set<AccessFlag>,
    override val name: String,
    override val descriptor: String,
    override val attributes: List<AttributeApi>
) : MethodApi {
    override fun lines(): List<String> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}