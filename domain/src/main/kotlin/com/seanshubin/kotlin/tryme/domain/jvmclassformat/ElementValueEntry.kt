package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

interface ElementValueEntry {
    val tag: ElementValueTag
    fun toObject(): Map<String, Any>

    data class ElementValueConstant(override val tag: ElementValueTag, val value: ConstantPoolEntry) :
        ElementValueEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.toString(),
                "value" to value.toObject()
            )
        }
    }

    data class ElementValueClass(
        override val tag: ElementValueTag,
        val classEntry: ConstantPoolEntry.ConstantPoolEntryClass
    ) : ElementValueEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.toString(),
                "classEntry" to classEntry.toObject()
            )
        }
    }

    data class ElementValueEnum(
        override val tag: ElementValueTag,
        val typeName: ConstantPoolEntry.ConstantPoolEntryUtf8,
        val constName: ConstantPoolEntry.ConstantPoolEntryUtf8
    ) : ElementValueEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.toString(),
                "typeName" to typeName.toObject(),
                "constName" to constName.toObject()
            )
        }
    }

    data class ElementValueAnnotation(override val tag: ElementValueTag, val annotation: AnnotationEntry) :
        ElementValueEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.toString(),
                "annotation" to annotation.toObject()
            )
        }
    }

    data class ElementValueArray(override val tag: ElementValueTag, val elementValues: List<ElementValueEntry>) :
        ElementValueEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.toString(),
                "elementValues" to elementValues.map { it.toObject() }
            )
        }
    }

    companion object {
        fun fromDataInput(input: DataInput, constantPoolMap: Map<Int, ConstantPoolEntry>): ElementValueEntry {
            val tagByte = input.readByte()
            val tag = ElementValueTag.fromByte(tagByte)
            val tagChar = tag.charValue
            return when (tagChar) {
                'B', 'C', 'D', 'F', 'I', 'J', 'S', 'Z', 's' -> {
                    val index = input.readUnsignedShort()
                    val constantEntry = constantPoolMap.getValue(index)
                    ElementValueConstant(tag, constantEntry)
                }

                'e' -> {
                    val typeNameIndex = input.readUnsignedShort()
                    val constNameIndex = input.readUnsignedShort()
                    val typeNameEntry =
                        constantPoolMap.getValue(typeNameIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                    val constNameEntry =
                        constantPoolMap.getValue(constNameIndex) as ConstantPoolEntry.ConstantPoolEntryUtf8
                    ElementValueEnum(tag, typeNameEntry, constNameEntry)
                }

                'c' -> {
                    val classIndex = input.readUnsignedShort()
                    val classEntry = constantPoolMap.getValue(classIndex) as ConstantPoolEntry.ConstantPoolEntryClass
                    ElementValueClass(tag, classEntry)
                }

                '@' -> {
                    val annotation = AnnotationEntry.fromDataInput(input, constantPoolMap)
                    ElementValueAnnotation(tag, annotation)
                }

                '[' -> {
                    val numValues = input.readShort()
                    val elementValues = List(numValues.toInt()) {
                        fromDataInput(input, constantPoolMap)
                    }
                    ElementValueArray(tag, elementValues)
                }

                else -> throw IllegalArgumentException("Unknown tag: $tag")
            }
        }
    }
}