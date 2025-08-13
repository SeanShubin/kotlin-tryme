package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoLong
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantLong
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil

object ConstantPoolLongApiFactory : ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.LongApi {
        info as ConstantPoolInfoLong
        val value = ByteUtil.bytesToLong(info.highBytes + info.lowBytes)
        return ConstantLong(
            info.tag,
            info.index,
            value
        )
    }
}
