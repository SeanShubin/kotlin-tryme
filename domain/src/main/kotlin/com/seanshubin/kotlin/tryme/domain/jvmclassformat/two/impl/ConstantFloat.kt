package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantFloat(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val value: Float
) : ConstantPoolApi.FloatApi {
    override fun line(): String {
        return makeLine(value.toString())
    }
}
