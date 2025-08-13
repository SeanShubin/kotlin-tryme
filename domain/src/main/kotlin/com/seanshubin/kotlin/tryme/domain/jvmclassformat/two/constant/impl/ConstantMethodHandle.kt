package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ReferenceKind
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantMethodHandle(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val referenceKind: ReferenceKind,
    override val className: String,
    override val methodName: String,
    override val methodDescriptor: String
) : ConstantPoolApi.MethodHandleApi {
    override fun line(): String {
        return makeLine("${referenceKind.line()} $className.$methodName $methodDescriptor")
    }
}
