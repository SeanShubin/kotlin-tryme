package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import java.io.DataInput

object ConstantPoolFactoryUtf8: ConstantPoolFactory {
    override fun fromDataInput(dataInput: DataInput): ConstantPoolApi {
        throw UnsupportedOperationException("Not Implemented!")
    }
}
