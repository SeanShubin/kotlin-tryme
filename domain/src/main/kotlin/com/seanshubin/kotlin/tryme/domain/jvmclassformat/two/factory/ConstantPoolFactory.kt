package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import java.io.DataInput

interface ConstantPoolFactory {
    fun fromDataInput(dataInput: DataInput): ConstantPoolApi
}
