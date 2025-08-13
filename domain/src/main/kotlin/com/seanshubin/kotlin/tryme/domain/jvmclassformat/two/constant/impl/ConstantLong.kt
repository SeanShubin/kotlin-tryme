package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantLong(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val value: Long
) : ConstantPoolApi.LongApi {
    override fun line(): String {
        return makeLine(value.toString())
    }
}
