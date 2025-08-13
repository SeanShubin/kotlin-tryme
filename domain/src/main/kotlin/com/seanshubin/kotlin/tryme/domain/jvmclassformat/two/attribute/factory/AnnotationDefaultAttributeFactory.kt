package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.attribute.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AttributeInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

object AnnotationDefaultAttributeFactory: AttributeFactory {
    override fun create(
        attributeInfo: AttributeInfo,
        constantPoolMap: Map<Int, ConstantPoolApi>
    ): AttributeApi {
        throw UnsupportedOperationException("Not Implemented!")
    }
}