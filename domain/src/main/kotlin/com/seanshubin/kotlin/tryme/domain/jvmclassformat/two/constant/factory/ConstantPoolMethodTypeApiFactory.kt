package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoMethodType
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantMethodType

object ConstantPoolMethodTypeApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.MethodTypeApi {
        info as ConstantPoolInfoMethodType
        val descriptorIndex = info.descriptorIndex.toInt()
        val utf8Info = constantPoolInfoMap.getValue(descriptorIndex) as ConstantPoolInfoUtf8
        val descriptor = String(utf8Info.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantMethodType(
            info.tag,
            info.index,
            descriptor
        )
    }
}
