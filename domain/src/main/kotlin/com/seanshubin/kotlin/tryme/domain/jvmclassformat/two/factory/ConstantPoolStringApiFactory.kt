package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoString
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantString

object ConstantPoolStringApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.StringApi {
        info as ConstantPoolInfoString
        val stringIndex = info.stringIndex.toInt()
        val utf8Info = constantPoolInfoMap.getValue(stringIndex) as ConstantPoolInfoUtf8
        val value = String(utf8Info.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantString(
            info.tag,
            info.index,
            value
        )
    }
}
