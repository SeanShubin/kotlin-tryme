package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

interface ConstantPoolInfoFactory {
    fun fromDataInput(input: DataInput, index: Int, tag: ConstantPoolTag): ConstantPoolInfo

    companion object {
        val constantPoolFactoryMap: Map<ConstantPoolTag, ConstantPoolInfoFactory> = mapOf(
            ConstantPoolTag.UTF8 to ConstantPoolInfoUtf8Factory,
            ConstantPoolTag.INTEGER to ConstantPoolInfoIntegerFactory,
            ConstantPoolTag.FLOAT to ConstantPoolInfoFloatFactory,
            ConstantPoolTag.LONG to ConstantPoolInfoLongFactory,
            ConstantPoolTag.DOUBLE to ConstantPoolInfoDoubleFactory,
            ConstantPoolTag.CLASS to ConstantPoolInfoClassFactory,
            ConstantPoolTag.STRING to ConstantPoolInfoStringFactory,
            ConstantPoolTag.FIELDREF to ConstantPoolInfoRefFactory,
            ConstantPoolTag.METHODREF to ConstantPoolInfoRefFactory,
            ConstantPoolTag.INTERFACEMETHODREF to ConstantPoolInfoRefFactory,
            ConstantPoolTag.NAMEANDTYPE to ConstantPoolInfoNameAndTypeFactory,
            ConstantPoolTag.METHODHANDLE to ConstantPoolInfoMethodHandleFactory,
            ConstantPoolTag.METHODTYPE to ConstantPoolInfoMethodTypeFactory,
            ConstantPoolTag.DYNAMIC to ConstantPoolInfoDynamicFactory,
            ConstantPoolTag.INVOKEDYNAMIC to ConstantPoolInfoDynamicFactory,
            ConstantPoolTag.MODULE to ConstantPoolInfoModuleFactory,
            ConstantPoolTag.PACKAGE to ConstantPoolInfoPackageFactory
        )

        fun fromDataInput(input: DataInput, index: Int): ConstantPoolInfo {
            val tag = ConstantPoolTag.fromDataInput(input)
            val factory = constantPoolFactoryMap[tag]
                ?: throw UnsupportedOperationException("No factory for tag $tag")
            return factory.fromDataInput(input, index, tag)
        }
    }
}
