package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoClass
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.impl.ConstantClass

object ConstantPoolClassApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.ClassApi {
        info as ConstantPoolInfoClass
        val nameIndex = info.nameIndex.toInt()
        val nameInfo = constantPoolInfoMap.getValue(nameIndex) as ConstantPoolInfoUtf8
        val name = String(nameInfo.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantClass(
            info.tag,
            info.index,
            name
        )
    }
}
