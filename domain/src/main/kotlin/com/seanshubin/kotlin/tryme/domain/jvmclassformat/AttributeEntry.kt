package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

interface AttributeEntry {
    val info: AttributeInfo
    val name: ConstantPoolEntry.ConstantPoolEntryUtf8
    fun toObject(): Map<String, Any>;

    data class RuntimeAnnotationsEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val annotations: List<AnnotationEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "annotations" to annotations.map { it.toObject() }
            )
        }
    }

    data class RuntimeParameterAnnotationsEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val parameterAnnotations: List<List<AnnotationEntry>>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "parameterAnnotations" to parameterAnnotations.map { annotations -> annotations.map { it.toObject() } }
            )
        }
    }

    data class CodeEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val maxStack: UShort,
        val maxLocals: UShort,
        val codeBlock: CodeBlock,
        val exceptionTable: List<ExceptionTableEntry>,
        val attributes: List<AttributeEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "maxStack" to maxStack,
                "maxLocals" to maxLocals,
                "codeBlock" to codeBlock.toObject(),
                "exceptionTable" to exceptionTable.map { it.toObject() },
                "attributes" to attributes.map { it.toObject() }
            )
        }

        companion object {
            fun byteToHexCode(byte: Byte): String {
                return String.format("%02X", byte)
            }
        }
    }

    data class LineNumberEntry(
        val startPc: UShort,
        val lineNumber: UShort
    ) {
        fun toObject(): Map<String, Any> {
            return mapOf(
                "startPc" to startPc.toInt(),
                "lineNumber" to lineNumber.toInt()
            )
        }

        companion object {
            fun fromDataInput(input: DataInput): LineNumberEntry {
                val startPc = input.readUnsignedShort().toUShort()
                val lineNumber = input.readUnsignedShort().toUShort()
                return LineNumberEntry(startPc, lineNumber)
            }
        }
    }

    data class LineNumberTableEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val lineNumbers: List<LineNumberEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "lineNumbers" to lineNumbers.map { it.toObject() }
            )
        }
    }

    data class LocalVariableEntry(
        val startPc: UShort,
        val length: UShort,
        val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val descriptor: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val index: UShort
    ) {
        fun toObject(): Map<String, Any> {
            return mapOf(
                "startPc" to startPc.toInt(),
                "length" to length.toInt(),
                "name" to name.toObject(),
                "descriptor" to descriptor.toObject(),
                "index" to index.toInt()
            )
        }

        companion object {
            fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): LocalVariableEntry {
                val startPc = input.readUnsignedShort().toUShort()
                val length = input.readUnsignedShort().toUShort()
                val nameIndex = input.readUnsignedShort().toInt()
                val name = constantPoolMap.getValue(nameIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                val descriptorIndex = input.readUnsignedShort().toInt()
                val descriptor = constantPoolMap.getValue(descriptorIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                val index = input.readUnsignedShort().toUShort()
                return LocalVariableEntry(startPc, length, name, descriptor, index)
            }
        }
    }

    data class LocalVariableTableEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val localVariables: List<LocalVariableEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "localVariables" to localVariables.map { it.toObject() }
            )
        }
    }

    data class SourceFileEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val sourceFile: ConstantPoolEntry.ConstantPoolEntryUtf8
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "sourceFile" to sourceFile.toObject()
            )
        }
    }

    data class BootstrapMethodEntry(
        val bootstrapMethodRef: ConstantPoolEntry.ConstantPoolEntryMethodHandle,
        val bootstrapArguments: List<ConstantPoolEntry.ConstantPoolEntryString>
    ) {
        fun toObject(): Map<String, Any> {
            return mapOf(
                "bootstrapMethodRef" to bootstrapMethodRef.toObject(),
                "bootstrapArguments" to bootstrapArguments.map { it.toObject() }
            )
        }

        companion object {
            fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): BootstrapMethodEntry {
                val bootstrapMethodRefIndex = input.readUnsignedShort().toInt()
                val bootstrapMethodRef =
                    constantPoolMap.getValue(bootstrapMethodRefIndex) as ConstantPoolEntry.ConstantPoolEntryMethodHandle
                val numBootstrapArguments = input.readUnsignedShort().toInt()
                val bootstrapArguments = List(numBootstrapArguments) {
                    val index = input.readUnsignedShort().toInt()
                    constantPoolMap.getValue(index) as ConstantPoolEntry.ConstantPoolEntryString
                }
                return BootstrapMethodEntry(bootstrapMethodRef, bootstrapArguments)
            }
        }
    }

    data class BootstrapMethodsEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val bootstrapMethods: List<BootstrapMethodEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "bootstrapMethods" to bootstrapMethods.map { it.toObject() }
            )
        }
    }

    companion object {
        fun fromAttributeInfo(raw: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val attributeEntry = AttributeEnum.Companion.fromAttributeInfo(raw, constantPoolMap)
            return attributeEntry
        }
    }
}