package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.io.DataInput

@OptIn(ExperimentalUnsignedTypes::class)
enum class ConstantPoolTag(val tagName: String, val tagId: UByte) {
    TagUtf8("Utf8", 1u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val length = input.readUnsignedShort()
            val bytes = ByteArray(length)
            input.readFully(bytes)
            val value = String(bytes, Charsets.UTF_8)
            return ConstantPoolInfo.ConstantUtf8(this, index, value)
        }
    },
    TagInteger("Integer", 3u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val value = input.readInt()
            return ConstantPoolInfo.ConstantInteger(this, index, value)
        }
    },
    TagFloat("Float", 4u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val value: Float = input.readFloat()
            return ConstantPoolInfo.ConstantFloat(this, index, value)
        }
    },
    TagLong("Long", 5u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val value = input.readLong()
            return ConstantPoolInfo.ConstantLong(this, index, value)
        }
    },
    TagDouble("Double", 6u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val value = input.readDouble()
            return ConstantPoolInfo.ConstantDouble(this, index, value)
        }
    },
    TagClass("Class", 7u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantClass(this, index, nameIndex)
        }
    },
    TagString("String", 8u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val stringIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantString(this, index, stringIndex)
        }
    },
    TagFieldref("Fieldref", 9u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantFieldRef(this, index, classIndex, nameAndTypeIndex)
        }
    },
    TagMethodref("Methodref", 10u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantMethodRef(this, index, classIndex, nameAndTypeIndex)
        }
    },
    TagInterfaceMethodref("InterfaceMethodref", 11u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantInterfaceMethodRef(this, index, classIndex, nameAndTypeIndex)
        }
    },
    TagNameAndType("NameAndType", 12u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantNameAndType(this, index, nameIndex, descriptorIndex)
        }
    },
    TagMethodHandle("MethodHandle", 15u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val referenceKind = input.readUnsignedByte().toUByte()
            val referenceIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantMethodHandle(this, index, referenceKind, referenceIndex)
        }
    },
    TagMethodType("MethodType", 16u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantMethodType(this, index, descriptorIndex)
        }
    },
    TagDynamic("Dynamic", 17u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantDynamic(this, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    },
    TagInvokeDynamic("InvokeDynamic", 18u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantInvokeDynamic(this, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    },
    TagModule("Module", 19u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantModule(this, index, nameIndex)
        }
    },
    TagPackage("Package", 20u) {
        override fun load(input: DataInput, index: UShort): ConstantPoolInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantPoolInfo.ConstantPackage(this, index, nameIndex)
        }
    };

    abstract fun load(input: DataInput, index: UShort): ConstantPoolInfo;
    override fun toString(): String = "$tagName($tagId)"

    companion object {
        fun fromByte(byte: UByte): ConstantPoolTag {
            return entries.firstOrNull { it.tagId == byte }
                ?: throw IllegalArgumentException("Unknown constant pool tag: $byte")
        }
    }
}
