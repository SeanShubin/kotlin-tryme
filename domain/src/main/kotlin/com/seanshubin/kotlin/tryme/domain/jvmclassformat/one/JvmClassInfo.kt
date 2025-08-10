package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

class JvmClassInfo(
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
    fun lines(): List<String> {
        return listOf(
            "magic: $magic",
            "minorVersion: $minorVersion",
            "majorVersion: $majorVersion",
            "constantPoolCountPlusOne: $constantPoolCountPlusOne"
        ) + constantPoolLines().map { "  $it" } +
                listOf(
                    "constantPool: ${constantPool.joinToString("\n")}",
                    "accessFlags: $accessFlags",
                    "thisClass: $thisClass",
                    "superClass: $superClass",
                    "interfacesCount: $interfacesCount",
                    "interfaces: ${interfaces.joinToString(", ")}",
                    "fieldCount: $fieldCount",
                    "fields: ${fields.joinToString("\n")}",
                    "methodCount: $methodCount",
                    "methods: ${methods.joinToString("\n")}",
                    "attributeCount: $attributeCount",
                    "attributes: ${attributes.joinToString("\n")}"
                )
    }

    private fun constantPoolLines(): List<String> {
        return constantPool.map { it.line() }
    }

    companion object {
        fun fromDataInput(input: DataInput): JvmClassInfo {
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
            val rawJvmClass = JvmClassInfo(
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
            return ConstantPoolInfoListFactory.fromDataInput(input, constantPoolCount)
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
