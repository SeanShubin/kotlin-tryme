package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

data class ConstantModule(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val name: String
) : ConstantPoolApi.ModuleApi {
    override fun line(): String {
        return makeLine(name)
    }
}
