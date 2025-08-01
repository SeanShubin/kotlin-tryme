package com.seanshubin.kotlin.tryme.domain.jvmclassformat

sealed interface ConstantPoolInfo {
    val tag: ConstantPoolTag
    val index: UShort

    data class ConstantUtf8(override val tag: ConstantPoolTag, override val index: UShort, val value: String) :
        ConstantPoolInfo

    data class ConstantInteger(override val tag: ConstantPoolTag, override val index: UShort, val value: Int) :
        ConstantPoolInfo

    data class ConstantFloat(override val tag: ConstantPoolTag, override val index: UShort, val value: Float) :
        ConstantPoolInfo

    data class ConstantLong(override val tag: ConstantPoolTag, override val index: UShort, val value: Long) :
        ConstantPoolInfo

    data class ConstantDouble(override val tag: ConstantPoolTag, override val index: UShort, val value: Double) :
        ConstantPoolInfo

    data class ConstantClass(override val tag: ConstantPoolTag, override val index: UShort, val nameIndex: UShort) :
        ConstantPoolInfo

    data class ConstantString(override val tag: ConstantPoolTag, override val index: UShort, val stringIndex: UShort) :
        ConstantPoolInfo

    data class ConstantFieldRef(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val classIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantMethodRef(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val classIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantInterfaceMethodRef(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val classIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantNameAndType(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val nameIndex: UShort,
        val descriptorIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantMethodHandle(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val referenceKind: UByte,
        val referenceIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantMethodType(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val descriptorIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantDynamic(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantInvokeDynamic(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo

    data class ConstantModule(override val tag: ConstantPoolTag, override val index: UShort, val nameIndex: UShort) :
        ConstantPoolInfo

    data class ConstantPackage(override val tag: ConstantPoolTag, override val index: UShort, val nameIndex: UShort) :
        ConstantPoolInfo
}
