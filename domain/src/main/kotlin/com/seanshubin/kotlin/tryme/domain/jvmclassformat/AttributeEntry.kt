package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

interface AttributeEntry {
    val info: AttributeInfo
    val name: ConstantPoolEntry.ConstantPoolEntryUtf8
    fun toObject(): Map<String, Any>;

    data class ConstantValueEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val constantValue: ConstantPoolEntry
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "constantValue" to constantValue.toObject()
            )
        }
    }

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

    data class LocalVariableTypeTableEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val localVariableTypes: List<LocalVariableTypeEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "localVariableTypes" to localVariableTypes.map { it.toObject() }
            )
        }
    }

    data class LocalVariableTypeEntry(
        val startPc: UShort,
        val length: UShort,
        val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val signature: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val index: UShort
    ) {
        fun toObject(): Map<String, Any> {
            return mapOf(
                "startPc" to startPc.toInt(),
                "length" to length.toInt(),
                "name" to name.toObject(),
                "signature" to signature.toObject(),
                "index" to index.toInt()
            )
        }

        companion object {
            fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): LocalVariableTypeEntry {
                val startPc = input.readUnsignedShort().toUShort()
                val length = input.readUnsignedShort().toUShort()
                val nameIndex = input.readUnsignedShort().toInt()
                val name = constantPoolMap.getValue(nameIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                val signatureIndex = input.readUnsignedShort().toInt()
                val signature = constantPoolMap.getValue(signatureIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                val index = input.readUnsignedShort().toUShort()
                return LocalVariableTypeEntry(startPc, length, name, signature, index)
            }
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

    data class UnknownAttributeEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject()
            )
        }
    }

    data class BootstrapMethodEntry(
        val bootstrapMethodRef: ConstantPoolEntry.ConstantPoolEntryMethodHandle,
        val bootstrapArguments: List<ConstantPoolEntry>
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
                    constantPoolMap.getValue(index)
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

    data class ExceptionEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val exceptionClasses: List<ConstantPoolEntry.ConstantPoolEntryClass>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "exceptionClasses" to exceptionClasses.map { it.toObject() }
            )
        }
    }

    data class SignatureEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val signature: ConstantPoolEntry.ConstantPoolEntryUtf8
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "signature" to signature.toObject()
            )
        }
    }

    data class EnclosingMethodEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val theClass: ConstantPoolEntry.ConstantPoolEntryClass,
        val method: ConstantPoolEntry.ConstantPoolEntryNameAndType?
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "class" to theClass.toObject(),
                "method" to (method?.toObject() ?: "null")
            )
        }
    }

    data class NestHostEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val nestHost: ConstantPoolEntry.ConstantPoolEntryClass
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "nestHost" to nestHost.toObject()
            )
        }
    }

    data class InnerClassesEntry(
        override val info: AttributeInfo,
        override val name: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val innerClasses: List<InnerClassEntry>
    ) : AttributeEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "name" to name.toObject(),
                "innerClasses" to innerClasses.map { it.toObject() }
            )
        }
    }

    data class InnerClassEntry(
        val innerClass: ConstantPoolEntry.ConstantPoolEntryClass,
        val outerClass: ConstantPoolEntry.ConstantPoolEntryClass?,
        val innerName: ConstantPoolEntry.ConstantPoolEntryUtf8?,
        val accessFlags: Set<AccessFlag>
    ) {
        fun toObject(): Map<String, Any?> {
            return mapOf(
                "innerClass" to innerClass.toObject(),
                "outerClass" to outerClass?.toObject(),
                "innerName" to innerName?.toObject(),
                "accessFlags" to accessFlags.map { it.name }
            )
        }

        companion object {
            fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): InnerClassEntry {
                val innerClassIndex = input.readUnsignedShort().toInt()
                val innerClass = constantPoolMap.getValue(innerClassIndex) as ConstantPoolEntry.ConstantPoolEntryClass
                val outerClassIndex = input.readUnsignedShort().toInt()
                val outerClass =
                    if (outerClassIndex == 0) null else constantPoolMap.getValue(outerClassIndex) as ConstantPoolEntry.ConstantPoolEntryClass
                val innerNameIndex = input.readUnsignedShort().toInt()
                val innerName =
                    if (innerNameIndex == 0) null else constantPoolMap.getValue(innerNameIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                val accessFlagMask = input.readUnsignedShort().toInt()
                val accessFlags = AccessFlag.fromMask(accessFlagMask)
                return InnerClassEntry(innerClass, outerClass, innerName, accessFlags)
            }
        }
    }

    companion object {
        fun fromAttributeInfo(raw: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val attributeEntry = AttributeEnum.Companion.fromAttributeInfo(raw, constantPoolMap)
            return attributeEntry
        }
    }
}