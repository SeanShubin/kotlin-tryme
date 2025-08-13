package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoInteger
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantInteger
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil

object ConstantPoolIntegerApiFactory : ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.IntegerApi {
        info as ConstantPoolInfoInteger
        val value = ByteUtil.bytesToInt(info.bytes)
        return ConstantInteger(
            info.tag,
            info.index,
            value
        )
    }
}
