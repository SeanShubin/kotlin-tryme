package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInputStream

enum class AttributeEnum {
    ConstantValue {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val constantValueIndex = input.readUnsignedShort().toInt()
            val constantValue = constantPoolMap.getValue(constantValueIndex)
            return AttributeEntry.ConstantValueEntry(attributeInfo, name, constantValue)
        }
    },
    RuntimeInvisibleAnnotations {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            return parseRuntimeAnnotations(attributeInfo, constantPoolMap)
        }
    },
    RuntimeVisibleAnnotations {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            return parseRuntimeAnnotations(attributeInfo, constantPoolMap)
        }
    },
    RuntimeInvisibleParameterAnnotations {
        override fun parse(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<Int, ConstantPoolEntry>
        ): AttributeEntry {
            return parseRuntimeParameterAnnotations(attributeInfo, constantPoolMap)
        }
    },
    Code {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val maxStack = input.readUnsignedShort().toUShort()
            val maxLocals = input.readUnsignedShort().toUShort()
            val codeLength = input.readInt()
            val code = ByteArray(codeLength.toInt())
            input.readFully(code)
            val opCodes = OpCodeEntry.fromBytes(code.toList(), constantPoolMap)
            val codeBlock = CodeBlock(opCodes)
            val exceptionTableLength = input.readUnsignedShort().toUShort()
            val exceptionTable = List(exceptionTableLength.toInt()) {
                ExceptionTableEntry.fromDataInput(input, constantPoolMap)
            }
            val attributesCount = input.readShort()
            val attributes = List(attributesCount.toInt()) {
                AttributeEntry.fromAttributeInfo(AttributeInfo.fromDataInput(input), constantPoolMap)
            }
            return AttributeEntry.CodeEntry(
                attributeInfo,
                name,
                maxStack,
                maxLocals,
                codeBlock,
                exceptionTable,
                attributes
            )
        }
    },
    LineNumberTable {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val lineNumberCount = input.readUnsignedShort().toInt()
            val lineNumbers = List(lineNumberCount) {
                AttributeEntry.LineNumberEntry.fromDataInput(input)
            }
            return AttributeEntry.LineNumberTableEntry(attributeInfo, name, lineNumbers)
        }
    },
    LocalVariableTable {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val localVariableCount = input.readUnsignedShort().toInt()
            val localVariables = List(localVariableCount) {
                AttributeEntry.LocalVariableEntry.fromDataInput(input, constantPoolMap)
            }
            return AttributeEntry.LocalVariableTableEntry(attributeInfo, name, localVariables)
        }
    },
    SourceFile {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val sourceFileIndex = input.readUnsignedShort().toInt()
            val sourceFile = constantPoolMap.getValue(sourceFileIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
            return AttributeEntry.SourceFileEntry(attributeInfo, name, sourceFile)
        }
    },
    BootstrapMethods {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val numBootstrapMethods = input.readUnsignedShort()
            val bootstrapMethods = List(numBootstrapMethods.toInt()) {
                AttributeEntry.BootstrapMethodEntry.fromDataInput(input, constantPoolMap)
            }
            return AttributeEntry.BootstrapMethodsEntry(attributeInfo, name, bootstrapMethods)
        }
    },
    Exceptions {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val numberOfExceptions = input.readUnsignedShort().toInt()
            val exceptionIndexTable = List(numberOfExceptions) {
                input.readUnsignedShort().toInt()
            }
            val exceptionClasses = exceptionIndexTable.map {
                constantPoolMap.getValue(it) as ConstantPoolEntry.ConstantPoolEntryClass
            }
            return AttributeEntry.ExceptionEntry(attributeInfo, name, exceptionClasses)
        }
    },
    Signature {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val signatureIndex = input.readUnsignedShort().toInt()
            val signature = constantPoolMap.getValue(signatureIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
            return AttributeEntry.SignatureEntry(attributeInfo, name, signature)
        }
    },
    EnclosingMethod {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val classIndex = input.readUnsignedShort().toInt()
            val methodIndex = input.readUnsignedShort().toInt()
            val enclosingClass = constantPoolMap.getValue(classIndex) as ConstantPoolEntry.ConstantPoolEntryClass
            val enclosingMethod =
                if(methodIndex == 0) null else constantPoolMap.getValue(methodIndex) as ConstantPoolEntry.ConstantPoolEntryNameAndType
            return AttributeEntry.EnclosingMethodEntry(attributeInfo, name, enclosingClass, enclosingMethod)
        }
    },
    NestHost {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val hostClassIndex = input.readUnsignedShort().toInt()
            val hostClass = constantPoolMap.getValue(hostClassIndex) as ConstantPoolEntry.ConstantPoolEntryClass
            return AttributeEntry.NestHostEntry(attributeInfo, name, hostClass)
        }
    },
    InnerClasses {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val numClasses = input.readUnsignedShort().toInt()
            val innerClasses = List(numClasses) {
                AttributeEntry.InnerClassEntry.fromDataInput(input, constantPoolMap)
            }
            return AttributeEntry.InnerClassesEntry(attributeInfo, name, innerClasses)
        }
    },
    LocalVariableTypeTable {
        override fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val localVariableTypeCount = input.readUnsignedShort().toInt()
            val localVariableTypes = List(localVariableTypeCount) {
                AttributeEntry.LocalVariableTypeEntry.fromDataInput(input, constantPoolMap)
            }
            return AttributeEntry.LocalVariableTypeTableEntry(attributeInfo, name, localVariableTypes)
        }
    };

    abstract fun parse(attributeInfo: AttributeInfo, constantPoolMap: Map<Int, ConstantPoolEntry>): AttributeEntry

    companion object {
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<Int, ConstantPoolEntry>
        ): AttributeEntry {
            val attributeUtf8 =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val attributeName = attributeUtf8.raw.value
            if(!entries.map { it.name }.contains(attributeName)){
                return AttributeEntry.UnknownAttributeEntry(
                    attributeInfo,
                    attributeUtf8
                )
            }
            val attributeEnum = valueOf(attributeName)
            val attributeEntry = attributeEnum.parse(attributeInfo, constantPoolMap)
            return attributeEntry
        }

        private fun parseRuntimeAnnotations(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<Int, ConstantPoolEntry>
        ): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val numAnnotations = input.readShort()
            val annotations = List(numAnnotations.toInt()) {
                AnnotationEntry.fromDataInput(input, constantPoolMap)
            }
            return AttributeEntry.RuntimeAnnotationsEntry(
                attributeInfo,
                name,
                annotations
            )
        }

        private fun parseRuntimeParameterAnnotations(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<Int, ConstantPoolEntry>
        ): AttributeEntry {
            val name =
                constantPoolMap.getValue(attributeInfo.attributeNameIndex.toInt()) as ConstantPoolEntry.ConstantPoolEntryUtf8
            val input = DataInputStream(attributeInfo.info.toByteArray().inputStream())
            val numParameters = input.readUnsignedByte()
            val parameters = List(numParameters) {
                val numAnnotations = input.readShort()
                val annotations = List(numAnnotations.toInt()) {
                    AnnotationEntry.fromDataInput(input, constantPoolMap)
                }
                annotations
            }
            return AttributeEntry.RuntimeParameterAnnotationsEntry(
                attributeInfo,
                name,
                parameters
            )
        }
    }
}
