package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantDynamic(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val bootstrapMethodAttrIndex: UShort,
    override val name: String,
    override val descriptor: String
) : ConstantPoolApi.DynamicApi {
    override fun line(): String {
        return makeLine("$bootstrapMethodAttrIndex $name $descriptor")
    }
}
