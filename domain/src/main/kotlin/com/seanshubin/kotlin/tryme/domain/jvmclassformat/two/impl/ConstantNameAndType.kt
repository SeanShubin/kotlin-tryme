package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantNameAndType(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val name: String,
    override val descriptor: String
) : ConstantPoolApi.NameAndTypeApi {
    override fun line(): String {
        return makeLine("$name $descriptor")
    }
}
