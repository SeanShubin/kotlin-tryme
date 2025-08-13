package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoDouble
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantDouble
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil

object ConstantPoolDoubleApiFactory : ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.DoubleApi {
        info as ConstantPoolInfoDouble
        val value = ByteUtil.bytesToDouble(info.highBytes + info.lowBytes)
        return ConstantDouble(
            info.tag,
            info.index,
            value
        )
    }
}
