package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import java.io.DataInput

object AttributeListFactory {
    fun fromDataInput(dataInput: DataInput, constantPool: List<ConstantPoolApi>):List<AttributeApi> {
        throw UnsupportedOperationException("not implemented")
    }
}