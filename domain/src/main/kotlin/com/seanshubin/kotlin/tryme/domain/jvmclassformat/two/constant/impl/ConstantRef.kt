package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantRef(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val className: String,
    override val methodName: String,
    override val methodDescriptor: String
) : ConstantPoolApi.RefApi {
    override fun line(): String {
        return makeLine("$className.$methodName $methodDescriptor")
    }
}
