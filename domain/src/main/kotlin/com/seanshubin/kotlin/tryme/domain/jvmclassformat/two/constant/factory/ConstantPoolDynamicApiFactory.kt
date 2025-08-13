package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoDynamic
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoNameAndType
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantDynamic

object ConstantPoolDynamicApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.DynamicApi {
        info as ConstantPoolInfoDynamic
        val nameAndTypeIndex = info.nameAndTypeIndex.toInt()
        val nameAndTypeInfo = constantPoolInfoMap.getValue(nameAndTypeIndex) as ConstantPoolInfoNameAndType
        val methodNameIndex = nameAndTypeInfo.nameIndex.toInt()
        val methodNameInfo = constantPoolInfoMap.getValue(methodNameIndex) as ConstantPoolInfoUtf8
        val methodName = String(methodNameInfo.bytes.toByteArray(), Charsets.UTF_8)
        val methodDescriptorIndex = nameAndTypeInfo.descriptorIndex.toInt()
        val methodDescriptorInfo = constantPoolInfoMap.getValue(methodDescriptorIndex) as ConstantPoolInfoUtf8
        val methodDescriptor = String(methodDescriptorInfo.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantDynamic(
            tag = info.tag,
            index = info.index,
            bootstrapMethodAttrIndex = info.bootstrapMethodAttrIndex,
            name = methodName,
            descriptor = methodDescriptor
        )
    }
}
