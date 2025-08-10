package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.MethodApi
import java.io.DataInput

object MethodListFactory {
    fun fromDataInput(dataInput: DataInput, constantPool: List<ConstantPoolApi>):List<MethodApi> {
        throw UnsupportedOperationException("not implemented")
    }
}