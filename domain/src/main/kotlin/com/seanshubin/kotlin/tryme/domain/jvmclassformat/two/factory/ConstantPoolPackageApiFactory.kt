package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoModule
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoPackage
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolInfoUtf8
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantModule
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.ConstantPackage

object ConstantPoolPackageApiFactory:ConstantPoolFactory {
    override fun create(
        info: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi.PackageApi {
        info as ConstantPoolInfoPackage
        val nameIndex = info.nameIndex.toInt()
        val utf8Info = constantPoolInfoMap.getValue(nameIndex) as ConstantPoolInfoUtf8
        val name = String(utf8Info.bytes.toByteArray(), Charsets.UTF_8)
        return ConstantPackage(
            info.tag,
            info.index,
            name
        )
    }
}
