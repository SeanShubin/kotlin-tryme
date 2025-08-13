package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoClass
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantClass
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantUtf8

object ConstantPoolUtf8ApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.Utf8Api {
        info as ConstantPoolInfoUtf8
        val value = String(info.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantUtf8(
            info.tag,
            info.index,
            value
        )
    }
}
