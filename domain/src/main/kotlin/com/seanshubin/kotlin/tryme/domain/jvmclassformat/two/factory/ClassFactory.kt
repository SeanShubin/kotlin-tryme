package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory


object ClassFactory {
//    fun fromDataInput(dataInput: DataInput): ClassApi {
//        val magic: UInt = dataInput.readInt().toUInt()
//        val minorVersion: UShort = dataInput.readUnsignedShort().toUShort()
//        val majorVersion: UShort = dataInput.readUnsignedShort().toUShort()
//        val constantPool = ConstantPoolListFactory.allConstants(dataInput)
//        val accessFlags = AccessFlag.fromMask(dataInput.readUnsignedShort().toUShort())
//        val thisClass = constantPool[dataInput.readUnsignedShort()] as ClassApi
//        val superClass = constantPool[dataInput.readUnsignedShort()] as ClassApi
//        val interfaces = List(dataInput.readUnsignedShort()) {
//            constantPool[dataInput.readUnsignedShort()] as ClassApi
//        }
//        val fields = FieldListFactory.fromDataInput(dataInput, constantPool)
//        val methods = MethodListFactory.fromDataInput(dataInput, constantPool)
//        val attributes = AttributeListFactory.fromDataInput(dataInput, constantPool)
//     return object:ClassApi {
//            override val magic: UInt = magic
//            override val minorVersion: UShort = minorVersion
//            override val majorVersion: UShort = majorVersion
//            override val constantPool: List<com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.ConstantPoolApi> = constantPool
//            override val accessFlags: Set<AccessFlag> = accessFlags
//            override val thisClass: com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.ConstantPoolApi.ClassApi = thisClass
//            override val superClass: com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.ConstantPoolApi.ClassApi = superClass
//            override val interfaces: List<com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.ConstantPoolApi.ClassApi> = interfaces
//            override val fields: List<com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.FieldApi> = fields
//            override val methods: List<com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.MethodApi> = methods
//            override val attributes: List<com.seanshubin.kotlin.tryme.domain.jvmclassformat.api.AttributeApi> = attributes
//     }
//    }
}