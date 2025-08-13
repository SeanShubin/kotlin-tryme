package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoClass
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoMethodHandle
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoNameAndType
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoRef
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantMethodHandle

object ConstantPoolMethodHandleApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.MethodHandleApi {
        info as ConstantPoolInfoMethodHandle
        val referenceIndex = info.referenceIndex.toInt()
        val referenceInfo = constantPoolInfoMap.getValue(referenceIndex) as ConstantPoolInfoRef
        val classIndex = referenceInfo.classIndex.toInt()
        val classInfo = constantPoolInfoMap.getValue(classIndex) as ConstantPoolInfoClass
        val classNameIndex = classInfo.nameIndex.toInt()
        val classNameInfo = constantPoolInfoMap.getValue(classNameIndex) as ConstantPoolInfoUtf8
        val className = String(classNameInfo.bytes.toByteArray(), Charsets.UTF_8)
        val nameAndTypeInfo = constantPoolInfoMap.getValue(referenceInfo.nameAndTypeIndex.toInt()) as ConstantPoolInfoNameAndType
        val methodNameIndex = nameAndTypeInfo.nameIndex.toInt()
        val methodNameInfo = constantPoolInfoMap.getValue(methodNameIndex) as ConstantPoolInfoUtf8
        val methodName = String(methodNameInfo.bytes.toByteArray(), Charsets.UTF_8)
        val methodDescriptorIndex = nameAndTypeInfo.descriptorIndex.toInt()
        val methodDescriptorInfo = constantPoolInfoMap.getValue(methodDescriptorIndex) as ConstantPoolInfoUtf8
        val methodDescriptor = String(methodDescriptorInfo.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantMethodHandle(
            tag = info.tag,
            index = info.index,
            referenceKind = info.referenceKind,
            className = className,
            methodName = methodName,
            methodDescriptor = methodDescriptor
        )
    }
}
