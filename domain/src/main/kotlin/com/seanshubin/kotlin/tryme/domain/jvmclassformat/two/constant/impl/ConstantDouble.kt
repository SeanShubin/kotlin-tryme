package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantDouble(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val value: Double
) : ConstantPoolApi.DoubleApi {
    override fun line(): String {
        return makeLine(value.toString())
    }
}
