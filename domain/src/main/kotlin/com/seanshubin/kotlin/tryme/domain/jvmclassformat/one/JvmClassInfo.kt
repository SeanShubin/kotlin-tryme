package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.intToBytes
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.indent
import java.io.DataInput

class JvmClassInfo(
    val magic: UInt,
    val minorVersion: UShort,
    val majorVersion: UShort,
    val constantPoolCountPlusOne: UShort,
    val constantPool: List<ConstantPoolInfo>,
    val accessFlags: Set<AccessFlag>,
    val thisClass: ConstantPoolInfoClass,
    val superClass: ConstantPoolInfoClass?,
    val interfacesCount: UShort,
    val interfaces: List<ConstantPoolInfoClass>,
    val fieldCount: UShort,
    val fields: List<FieldInfo>,
    val methodCount: UShort,
    val methods: List<MethodInfo>,
    val attributeCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(): List<String> {
        return listOf(
            "magic: ${bytesToHex(intToBytes(magic.toInt()))}",
            "minorVersion: $minorVersion",
            "majorVersion: $majorVersion",
            "constantPoolCountPlusOne: $constantPoolCountPlusOne",
            "accessFlags: $accessFlags",
            "thisClass: ${thisClass.line()}",
            "superClass: ${superClass?.line() ?: "null"}"
        ) + constantPoolLines() + interfaceLines() + fieldLines() + methodLines() + attributeLines()
    }

    private fun constantPoolLines(): List<String> {
        val header = "constant pool(${constantPool.size}):"
        val body = constantPool.map { it.line() }.map(indent)
        return listOf(header) + body
    }

    private fun interfaceLines(): List<String> {
        val header = "interfaces(${interfaces.size}):"
        val body = interfaces.map { it.line() }.map(indent)
        return listOf(header) + body
    }

    private fun methodLines():List<String>{
        val header = "methods(${methods.size}):"
        val body = methods.flatMapIndexed { index, methodInfo ->
            listOf("method[$index]:") + methodInfo.lines().map(indent)
        }.map(indent)
        return listOf(header) + body
    }

    private fun fieldLines(): List<String> {
        val header = "fields(${fields.size}):"
        val body = fields.flatMapIndexed { index, fieldInfo ->
            listOf("field[$index]:") + fieldInfo.lines()
        }.map(indent)
        return listOf(header) + body
    }

    private fun attributeLines():List<String>{
        val header = "attributes(${attributes.size}):"
        val body = attributes.flatMapIndexed { index, attributeInfo ->
            listOf("attribute[$index]:") + indent(attributeInfo.line())
        }.map(indent)
        return listOf(header) + body
    }

    companion object {
        fun fromDataInput(input: DataInput): JvmClassInfo {
            val magic = input.readInt().toUInt()
            val minorVersion = input.readUnsignedShort().toUShort()
            val majorVersion = input.readUnsignedShort().toUShort()
            val constantPoolCount = input.readUnsignedShort().toUShort()
            val constantPool = readConstantPool(input, constantPoolCount)
            val constantsByIndex = constantPool.associateBy { it.index }
            val accessFlags = AccessFlag.fromDataInput(input)
            val thisClass = constantsByIndex.getValue(input.readUnsignedShort()) as ConstantPoolInfoClass
            val superClassIndex = input.readUnsignedShort()
            val superClass = if(superClassIndex==0) null else {
                constantsByIndex.getValue(superClassIndex) as ConstantPoolInfoClass
            }
            val interfacesCount = input.readUnsignedShort().toUShort()
            val interfaces = readInterfaces(input, interfacesCount, constantsByIndex)
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

        fun readInterfaces(input: DataInput, interfacesCount: UShort, constantsByIndex:Map<Int, ConstantPoolInfo>): List<ConstantPoolInfoClass> {
            return (0 until interfacesCount.toInt()).map {
                val index = input.readUnsignedShort()
                constantsByIndex[index] as ConstantPoolInfoClass
            }
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
