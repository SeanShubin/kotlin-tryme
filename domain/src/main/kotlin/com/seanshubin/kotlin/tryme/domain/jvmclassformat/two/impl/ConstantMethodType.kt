package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantMethodType(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val descriptor: String
) : ConstantPoolApi.MethodTypeApi {
    override fun line(): String {
        return makeLine(descriptor)
    }
}
