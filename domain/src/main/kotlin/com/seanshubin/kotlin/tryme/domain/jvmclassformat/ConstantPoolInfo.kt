package com.seanshubin.kotlin.tryme.domain.jvmclassformat

sealed interface ConstantPoolInfo {
    val tag: ConstantPoolTag
    val index: UShort

    fun toObject(): Map<String, Any>

    fun entriesTaken(): Int {
        return tag.entriesTaken
    }

    data class ConstantUtf8(override val tag: ConstantPoolTag, override val index: UShort, val value: String) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "value" to value
            )
        }
    }

    data class ConstantInteger(override val tag: ConstantPoolTag, override val index: UShort, val value: Int) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "value" to value
            )
        }
    }

    data class ConstantFloat(override val tag: ConstantPoolTag, override val index: UShort, val value: Float) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "value" to value
            )
        }
    }


    data class ConstantLong(override val tag: ConstantPoolTag, override val index: UShort, val value: Long) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "value" to value
            )
        }
    }

    data class ConstantDouble(override val tag: ConstantPoolTag, override val index: UShort, val value: Double) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "value" to value
            )
        }
    }

    data class ConstantClass(override val tag: ConstantPoolTag, override val index: UShort, val nameIndex: UShort) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "nameIndex" to nameIndex.toInt()
            )
        }
    }

    data class ConstantString(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val stringIndex: UShort
    ) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "stringIndex" to stringIndex.toInt()
            )
        }
    }

    data class ConstantFieldMethodInterfaceMethodRef(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val classIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "classIndex" to classIndex.toInt(),
                "nameAndTypeIndex" to nameAndTypeIndex.toInt()
            )
        }
    }

    data class ConstantNameAndType(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val nameIndex: UShort,
        val descriptorIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "nameIndex" to nameIndex.toInt(),
                "descriptorIndex" to descriptorIndex.toInt()
            )
        }
    }


    data class ConstantMethodHandle(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val referenceKind: UByte,
        val referenceIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "referenceKind" to referenceKind.toInt(),
                "referenceIndex" to referenceIndex.toInt()
            )
        }
    }

    data class ConstantMethodType(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val descriptorIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "descriptorIndex" to descriptorIndex.toInt()
            )
        }
    }

    data class ConstantDynamic(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "bootstrapMethodAttrIndex" to bootstrapMethodAttrIndex.toInt(),
                "nameAndTypeIndex" to nameAndTypeIndex.toInt()
            )
        }
    }

    data class ConstantInvokeDynamic(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeIndex: UShort
    ) : ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "bootstrapMethodAttrIndex" to bootstrapMethodAttrIndex.toInt(),
                "nameAndTypeIndex" to nameAndTypeIndex.toInt()
            )
        }
    }

    data class ConstantModule(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val nameIndex: UShort
    ) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "nameIndex" to nameIndex.toInt()
            )
        }
    }

    data class ConstantPackage(
        override val tag: ConstantPoolTag,
        override val index: UShort,
        val nameIndex: UShort
    ) :
        ConstantPoolInfo {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "tag" to tag.name,
                "index" to index.toInt(),
                "nameIndex" to nameIndex.toInt()
            )
        }
    }
}
