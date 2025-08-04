package com.seanshubin.kotlin.tryme.domain.jvmclassformat

interface ConstantPoolEntry {
    val raw: ConstantPoolInfo
    fun toObject(): Map<String, Any>
    data class ConstantPoolEntryUtf8(override val raw: ConstantPoolInfo.ConstantUtf8) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to raw.value,
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantUtf8, lookup: ConstantPoolLookup): ConstantPoolEntry {
                return ConstantPoolEntryUtf8(raw)
            }
        }
    }

    data class ConstantPoolEntryInteger(override val raw: ConstantPoolInfo.ConstantInteger) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to raw.value,
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantInteger, lookup: ConstantPoolLookup): ConstantPoolEntry {
                return ConstantPoolEntryInteger(raw)
            }
        }
    }

    data class ConstantPoolEntryFloat(override val raw: ConstantPoolInfo.ConstantFloat) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to raw.value,
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantFloat, lookup: ConstantPoolLookup): ConstantPoolEntry {
                return ConstantPoolEntryFloat(raw)
            }
        }
    }

    data class ConstantPoolEntryLong(override val raw: ConstantPoolInfo.ConstantLong) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to raw.value,
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantLong, lookup: ConstantPoolLookup): ConstantPoolEntry {
                return ConstantPoolEntryLong(raw)
            }
        }
    }

    data class ConstantPoolEntryDouble(override val raw: ConstantPoolInfo.ConstantDouble) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to raw.value,
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantDouble, lookup: ConstantPoolLookup): ConstantPoolEntry {
                return ConstantPoolEntryDouble(raw)
            }
        }
    }

    data class ConstantPoolEntryClass(
        override val raw: ConstantPoolInfo.ConstantClass,
        val name: ConstantPoolEntryUtf8
    ) :
        ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "name" to name.toObject(),
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantClass, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val nameInfo = lookup.lookup(raw.nameIndex) as ConstantPoolInfo.ConstantUtf8
                val nameEntry = ConstantPoolEntryUtf8.parse(nameInfo, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryClass(raw, nameEntry)
            }
        }
    }

    data class ConstantPoolEntryString(
        override val raw: ConstantPoolInfo.ConstantString,
        val value: ConstantPoolEntryUtf8
    ) :
        ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "value" to value.toObject(),
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantString, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val valueInfo = lookup.lookup(raw.stringIndex) as ConstantPoolInfo.ConstantUtf8
                val valueEntry = ConstantPoolEntryUtf8.parse(valueInfo, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryString(raw, valueEntry)
            }
        }
    }

    data class ConstantPoolEntryFieldref(
        override val raw: ConstantPoolInfo.ConstantFieldRef,
        val classEntry: ConstantPoolEntryClass,
        val nameAndTypeEntry: ConstantPoolEntryNameAndType
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "class" to classEntry.toObject(),
                "nameAndType" to nameAndTypeEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantFieldRef, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val classInfo = lookup.lookup(raw.classIndex) as ConstantPoolInfo.ConstantClass
                val nameAndTypeInfo = lookup.lookup(raw.nameAndTypeIndex) as ConstantPoolInfo.ConstantNameAndType
                val classEntry = ConstantPoolEntryClass.parse(classInfo, lookup) as ConstantPoolEntryClass
                val nameAndTypeEntry =
                    ConstantPoolEntryNameAndType.parse(nameAndTypeInfo, lookup) as ConstantPoolEntryNameAndType
                return ConstantPoolEntryFieldref(
                    raw,
                    classEntry,
                    nameAndTypeEntry
                )
            }
        }
    }

    data class ConstantPoolEntryMethodref(
        override val raw: ConstantPoolInfo.ConstantMethodRef,
        val classEntry: ConstantPoolEntryClass,
        val nameAndTypeEntry: ConstantPoolEntryNameAndType
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "class" to classEntry.toObject(),
                "nameAndType" to nameAndTypeEntry.toObject()
            )
        }

        fun methodAddress(): String {
            val className = classEntry.name.raw.value
            val methodName = nameAndTypeEntry.nameEntry.raw.value
            val descriptor = nameAndTypeEntry.descriptorEntry.raw.value
            return "$className.$methodName$descriptor"
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantMethodRef, lookup: ConstantPoolLookup): ConstantPoolEntryMethodref {
                val classInfo = lookup.lookup(raw.classIndex) as ConstantPoolInfo.ConstantClass
                val nameAndTypeInfo = lookup.lookup(raw.nameAndTypeIndex) as ConstantPoolInfo.ConstantNameAndType
                val classEntry = ConstantPoolEntryClass.parse(classInfo, lookup) as ConstantPoolEntryClass
                val nameAndTypeEntry =
                    ConstantPoolEntryNameAndType.parse(nameAndTypeInfo, lookup) as ConstantPoolEntryNameAndType
                return ConstantPoolEntryMethodref(
                    raw,
                    classEntry,
                    nameAndTypeEntry
                )
            }
        }
    }

    data class ConstantPoolEntryInterfaceMethodref(
        override val raw: ConstantPoolInfo.ConstantInterfaceMethodRef,
        val classEntry: ConstantPoolEntryClass,
        val nameAndTypeEntry: ConstantPoolEntryNameAndType
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "classIndex" to raw.classIndex,
                "nameAndTypeIndex" to raw.nameAndTypeIndex
            )
        }

        fun methodAddress(): String {
            val className = classEntry.name.raw.value
            val methodName = nameAndTypeEntry.nameEntry.raw.value
            val descriptor = nameAndTypeEntry.descriptorEntry.raw.value
            return "$className.$methodName$descriptor"
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantInterfaceMethodRef, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val classInfo = lookup.lookup(raw.classIndex) as ConstantPoolInfo.ConstantClass
                val nameAndTypeInfo = lookup.lookup(raw.nameAndTypeIndex) as ConstantPoolInfo.ConstantNameAndType
                val classEntry = ConstantPoolEntryClass.parse(classInfo, lookup) as ConstantPoolEntryClass
                val nameAndTypeEntry =
                    ConstantPoolEntryNameAndType.parse(nameAndTypeInfo, lookup) as ConstantPoolEntryNameAndType
                return ConstantPoolEntryInterfaceMethodref(
                    raw,
                    classEntry,
                    nameAndTypeEntry
                )
            }
        }
    }

    data class ConstantPoolEntryNameAndType(
        override val raw: ConstantPoolInfo.ConstantNameAndType,
        val nameEntry: ConstantPoolEntryUtf8,
        val descriptorEntry: ConstantPoolEntryUtf8
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "name" to nameEntry.toObject(),
                "descriptor" to descriptorEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantNameAndType, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val name = lookup.lookup(raw.nameIndex) as ConstantPoolInfo.ConstantUtf8
                val nameEntry = ConstantPoolEntryUtf8.parse(name, lookup) as ConstantPoolEntryUtf8
                val descriptor = lookup.lookup(raw.descriptorIndex) as ConstantPoolInfo.ConstantUtf8
                val descriptorEntry = ConstantPoolEntryUtf8.parse(descriptor, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryNameAndType(raw, nameEntry, descriptorEntry)
            }
        }
    }

    data class ConstantPoolEntryMethodHandle(
        override val raw: ConstantPoolInfo.ConstantMethodHandle,
        val referenceKind: ReferenceKind,
        val referenceEntry: ConstantPoolEntryMethodref
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "referenceKind" to referenceKind.toString(),
                "reference" to referenceEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantMethodHandle, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val referenceKind = ReferenceKind.fromCode(raw.referenceKind)
                val referenceInfo = lookup.lookup(raw.referenceIndex) as ConstantPoolInfo.ConstantMethodRef
                val referenceEntry = ConstantPoolEntryMethodref.parse(referenceInfo, lookup)
                return ConstantPoolEntryMethodHandle(raw, referenceKind, referenceEntry)
            }
        }
    }

    data class ConstantPoolEntryMethodType(
        override val raw: ConstantPoolInfo.ConstantMethodType,
        val descriptorEntry: ConstantPoolEntryUtf8
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "descriptor" to descriptorEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantMethodType, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val descriptorInfo = lookup.lookup(raw.descriptorIndex) as ConstantPoolInfo.ConstantUtf8
                val descriptorEntry = ConstantPoolEntryUtf8.parse(descriptorInfo, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryMethodType(raw, descriptorEntry)
            }
        }
    }

    data class ConstantPoolEntryDynamic(
        override val raw: ConstantPoolInfo.ConstantDynamic,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeEntry: ConstantPoolEntryNameAndType
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "bootstrapMethodAttrIndex" to bootstrapMethodAttrIndex.toInt(),
                "nameAndType" to nameAndTypeEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantDynamic, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val nameAndTypeInfo = lookup.lookup(raw.nameAndTypeIndex) as ConstantPoolInfo.ConstantNameAndType
                val nameAndTypeEntry =
                    ConstantPoolEntryNameAndType.parse(nameAndTypeInfo, lookup) as ConstantPoolEntryNameAndType
                return ConstantPoolEntryDynamic(raw, raw.bootstrapMethodAttrIndex, nameAndTypeEntry)
            }
        }
    }

    data class ConstantPoolEntryInvokeDynamic(
        override val raw: ConstantPoolInfo.ConstantInvokeDynamic,
        val bootstrapMethodAttrIndex: UShort,
        val nameAndTypeEntry: ConstantPoolEntryNameAndType
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "bootstrapMethodAttrIndex" to bootstrapMethodAttrIndex.toInt(),
                "nameAndType" to nameAndTypeEntry.toObject()
            )
        }

        fun methodAddress(): String {
            val className = "(dynamic)"
            val methodName = nameAndTypeEntry.nameEntry.raw.value
            val descriptor = nameAndTypeEntry.descriptorEntry.raw.value
            return "$className.$methodName$descriptor"
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantInvokeDynamic, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val nameAndTypeInfo = lookup.lookup(raw.nameAndTypeIndex) as ConstantPoolInfo.ConstantNameAndType
                val nameAndTypeEntry =
                    ConstantPoolEntryNameAndType.parse(nameAndTypeInfo, lookup) as ConstantPoolEntryNameAndType
                return ConstantPoolEntryInvokeDynamic(raw, raw.bootstrapMethodAttrIndex, nameAndTypeEntry)
            }
        }
    }

    data class ConstantPoolEntryModule(
        override val raw: ConstantPoolInfo.ConstantModule,
        val nameEntry: ConstantPoolEntryUtf8
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "name" to nameEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantModule, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val nameInfo = lookup.lookup(raw.nameIndex) as ConstantPoolInfo.ConstantUtf8
                val nameEntry = ConstantPoolEntryUtf8.parse(nameInfo, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryModule(raw, nameEntry)
            }
        }
    }

    data class ConstantPoolEntryPackage(
        override val raw: ConstantPoolInfo.ConstantPackage,
        val nameEntry: ConstantPoolEntryUtf8
    ) : ConstantPoolEntry {
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to raw.index,
                "tag" to raw.tag.toString(),
                "name" to nameEntry.toObject()
            )
        }

        companion object {
            fun parse(raw: ConstantPoolInfo.ConstantPackage, lookup: ConstantPoolLookup): ConstantPoolEntry {
                val nameInfo = lookup.lookup(raw.nameIndex) as ConstantPoolInfo.ConstantUtf8
                val nameEntry = ConstantPoolEntryUtf8.parse(nameInfo, lookup) as ConstantPoolEntryUtf8
                return ConstantPoolEntryPackage(raw, nameEntry)
            }
        }
    }

    companion object {
        fun from(raw: ConstantPoolInfo, lookup: ConstantPoolLookup): ConstantPoolEntry {
            return when (raw) {
                is ConstantPoolInfo.ConstantUtf8 -> ConstantPoolEntryUtf8.parse(raw, lookup)
                is ConstantPoolInfo.ConstantInteger -> ConstantPoolEntryInteger.parse(raw, lookup)
                is ConstantPoolInfo.ConstantFloat -> ConstantPoolEntryFloat.parse(raw, lookup)
                is ConstantPoolInfo.ConstantLong -> ConstantPoolEntryLong.parse(raw, lookup)
                is ConstantPoolInfo.ConstantDouble -> ConstantPoolEntryDouble.parse(raw, lookup)
                is ConstantPoolInfo.ConstantClass -> ConstantPoolEntryClass.parse(raw, lookup)
                is ConstantPoolInfo.ConstantString -> ConstantPoolEntryString.parse(raw, lookup)
                is ConstantPoolInfo.ConstantFieldRef -> ConstantPoolEntryFieldref.parse(raw, lookup)
                is ConstantPoolInfo.ConstantMethodRef -> ConstantPoolEntryMethodref.parse(raw, lookup)
                is ConstantPoolInfo.ConstantInterfaceMethodRef -> ConstantPoolEntryInterfaceMethodref.parse(raw, lookup)
                is ConstantPoolInfo.ConstantNameAndType -> ConstantPoolEntryNameAndType.parse(raw, lookup)
                is ConstantPoolInfo.ConstantMethodHandle -> ConstantPoolEntryMethodHandle.parse(raw, lookup)
                is ConstantPoolInfo.ConstantMethodType -> ConstantPoolEntryMethodType.parse(raw, lookup)
                is ConstantPoolInfo.ConstantDynamic -> ConstantPoolEntryDynamic.parse(raw, lookup)
                is ConstantPoolInfo.ConstantInvokeDynamic -> ConstantPoolEntryInvokeDynamic.parse(raw, lookup)
                is ConstantPoolInfo.ConstantModule -> ConstantPoolEntryModule.parse(raw, lookup)
                is ConstantPoolInfo.ConstantPackage -> ConstantPoolEntryPackage.parse(raw, lookup)
            }
        }
    }
}

