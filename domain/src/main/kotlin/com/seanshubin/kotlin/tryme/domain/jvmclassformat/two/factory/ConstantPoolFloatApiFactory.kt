package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoFloat
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoInteger
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantFloat
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantInteger
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil

object ConstantPoolFloatApiFactory : ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.FloatApi {
        info as ConstantPoolInfoFloat
        val value = ByteUtil.bytesToFloat(info.bytes)
        return ConstantFloat(
            info.tag,
            info.index,
            value
        )
    }
}
