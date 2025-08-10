package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.FieldApi
import java.io.DataInput

object FieldListFactory {
    fun fromDataInput(dataInput: DataInput, constantPool: List<ConstantPoolApi>):List<FieldApi> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}