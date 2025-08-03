package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput


class RawJvmClass(
    val magic: UInt,
    val minorVersion: UShort,
    val majorVersion: UShort,
    val constantPoolCountPlusOne: UShort,
    val constantPool: List<ConstantPoolInfo>,
    val accessFlags: UShort,
    val thisClass: UShort,
    val superClass: UShort,
    val interfacesCount: UShort,
    val interfaces: List<UShort>,
    val fieldCount: UShort,
    val fields: List<FieldInfo>,
    val methodCount: UShort,
    val methods: List<MethodInfo>,
    val attributeCount: UShort,
    val attributes: List<AttributeInfo>
) {
    companion object {
        fun fromDataInput(input: DataInput): RawJvmClass {
            val magic = input.readInt().toUInt()
            val minorVersion = input.readUnsignedShort().toUShort()
            val majorVersion = input.readUnsignedShort().toUShort()
            val constantPoolCount = input.readUnsignedShort().toUShort()
            val constantPool = readConstantPool(input, constantPoolCount)
            val accessFlags = input.readUnsignedShort().toUShort()
            val thisClass = input.readUnsignedShort().toUShort()
            val superClass = input.readUnsignedShort().toUShort()
            val interfacesCount = input.readUnsignedShort().toUShort()
            val interfaces = readInterfaces(input, interfacesCount)
            val fieldsCount = input.readUnsignedShort().toUShort()
            val fields = readFields(input, fieldsCount)
            val methodCount = input.readUnsignedShort().toUShort()
            val methods = readMethods(input, methodCount)
            val attributeCount = input.readUnsignedShort().toUShort()
            val attributes = readAttributes(input, attributeCount)
            assertEndOfInput(input)
            val rawJvmClass = RawJvmClass(
                magic,
                minorVersion,
                majorVersion,
                constantPoolCount,
                constantPool,
                accessFlags,
                thisClass,
                superClass,
                interfacesCount,
                interfaces,
                fieldsCount,
                fields,
                methodCount,
                methods,
                attributeCount,
                attributes
            )
            return rawJvmClass
        }

        fun readConstantPool(input: DataInput, constantPoolCount: UShort): List<ConstantPoolInfo> {
            var index = 1
            val result = mutableListOf<ConstantPoolInfo>()
            while(index < constantPoolCount.toInt()){
                val constant = readConstant(input, index)
                index += constant.entriesTaken()
                result.add(constant)
            }
            return result
        }

        fun readConstant(input: DataInput, index: Int): ConstantPoolInfo {
            val tagByte = input.readUnsignedByte()
            val tag = ConstantPoolTag.fromByte(tagByte.toUByte())
            return tag.load(input, index.toUShort())
        }

        fun readInterfaces(input: DataInput, interfacesCount: UShort): List<UShort> {
            return (0 until interfacesCount.toInt()).map { input.readUnsignedShort().toUShort() }
        }

        fun readFields(input: DataInput, fieldsCount: UShort): List<FieldInfo> {
            return (0 until fieldsCount.toInt()).map { FieldInfo.fromDataInput(input) }
        }

        fun readMethods(input: DataInput, methodCount: UShort): List<MethodInfo> {
            return (0 until methodCount.toInt()).map { MethodInfo.fromDataInput(input) }
        }

        fun readAttributes(input: DataInput, attributeCount: UShort): List<AttributeInfo> {
            return (0 until attributeCount.toInt()).map { AttributeInfo.fromDataInput(input) }
        }

        fun assertEndOfInput(input: DataInput) {
            try {
                input.readByte()
                throw RuntimeException("Expected end of input, but found more data")
            } catch (e: Exception) {
                // do nothing
            }
        }

    }
}