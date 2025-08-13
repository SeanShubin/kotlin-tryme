package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantInteger(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val value: Int
) : ConstantPoolApi.IntegerApi {
    override fun line(): String {
        return makeLine(value.toString())
    }
}
