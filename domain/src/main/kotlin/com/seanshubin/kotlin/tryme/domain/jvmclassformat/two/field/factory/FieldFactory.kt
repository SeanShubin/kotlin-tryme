package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.field.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AccessFlag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FieldInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.FieldApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.attribute.factory.AttributeFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.attribute.factory.AttributeFactory.Companion.attributeFactoryMap
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.field.impl.FieldImpl
import kotlin.UShort

object FieldFactory {
    fun create(info: FieldInfo, constantPoolMap:Map<Int, ConstantPoolApi>): FieldApi {
        val nameIndex = info.nameIndex.toInt()
        val nameConstant = constantPoolMap.getValue(nameIndex) as ConstantPoolApi.Utf8Api
        val name = nameConstant.value
        val descriptorIndex = info.descriptorIndex.toInt()
        val descriptorConstant = constantPoolMap.getValue(descriptorIndex) as ConstantPoolApi.Utf8Api
        val descriptor = descriptorConstant.value
        val attributesCount = info.attributesCount.toInt()
        val attributes = (0 until attributesCount).map {
            val attributeNameIndex = info.attributes[it].attributeNameIndex.toInt()
            val attributeNameConstant = constantPoolMap.getValue(attributeNameIndex) as ConstantPoolApi.Utf8Api
            val attributeName = attributeNameConstant.value
            val attributeFactory = attributeFactoryMap.getValue(attributeName)
            attributeFactory.create(info.attributes[it], constantPoolMap)
        }
        return FieldImpl(
            info.accessFlags,
            name,
            descriptor,
            attributes
        )
    }
}