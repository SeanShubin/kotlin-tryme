package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoNameAndType
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantNameAndType

object ConstantPoolNameAndTypeApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.NameAndTypeApi {
        info as ConstantPoolInfoNameAndType
        val methodNameIndex = info.nameIndex.toInt()
        val methodNameInfo = constantPoolInfoMap.getValue(methodNameIndex) as ConstantPoolInfoUtf8
        val methodName = String(methodNameInfo.bytes.toByteArray(), Charsets.UTF_8)
        val methodDescriptorIndex = info.descriptorIndex.toInt()
        val methodDescriptorInfo = constantPoolInfoMap.getValue(methodDescriptorIndex) as ConstantPoolInfoUtf8
        val methodDescriptor = String(methodDescriptorInfo.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantNameAndType(
            tag = info.tag,
            index = info.index,
            name = methodName,
            descriptor = methodDescriptor
        )
    }
}
