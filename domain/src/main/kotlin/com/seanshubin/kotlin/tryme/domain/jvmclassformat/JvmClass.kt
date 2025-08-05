package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class JvmClass(
    val magic: Int,
    val minorVersion: Int,
    val majorVersion: Int,
    val constantPool: List<ConstantPoolEntry>,
    val accessFlags: Set<AccessFlag>,
    val thisClass: ConstantPoolEntry.ConstantPoolEntryClass,
    val superClass: ConstantPoolEntry.ConstantPoolEntryClass?,
    val interfaces: List<ConstantPoolEntry.ConstantPoolEntryClass>,
    val fields: List<FieldEntry>,
    val methods: List<MethodEntry>,
    val attributes: List<AttributeEntry>
) {
    fun methodDependencies(): List<Pair<String, List<String>>> {
        return methods.map { method ->
            val className = thisClass.name.raw.value
            val name = method.name.raw.value
            val descriptor = method.descriptor.raw.value
            val codeBlock = method.codeAttribute?.codeBlock
            val methodDoingCall = "$className.$name$descriptor"
            if (codeBlock == null) {
                Pair("(no code) $methodDoingCall", emptyList())
            } else {
                val methodsBeingCalled = codeBlock.opCodes.mapNotNull { opCode ->
                    val code = opCode.code
                    when (code) {
                        Code.invokestatic -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry = opCode.constantPoolEntry
                            constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                            "(static   ) ${constantPoolEntry.methodAddress()}"
                        }

                        Code.invokespecial -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry = opCode.constantPoolEntry
                            constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                            "(special  ) ${constantPoolEntry.methodAddress()}"
                        }

                        Code.invokevirtual -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry =
                                opCode.constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                            "(virtual  ) ${constantPoolEntry.methodAddress()}"
                        }

                        Code.invokeinterface -> {
                            opCode as OpCodeEntry.MethodRefAndArgCount
                            val constantPoolEntry = opCode.methodRef
                            "(interface) ${constantPoolEntry.methodAddress()}"
                        }

                        Code.invokedynamic -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry =
                                opCode.constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryInvokeDynamic
                            "(dynamic  ) ${constantPoolEntry.methodAddress()}"
                        }

                        Code.getstatic -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry = opCode.constantPoolEntry
                            constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                            "(getstatic) ${constantPoolEntry.methodAddress()}"
                        }

                        else -> null
                    }
                }
                Pair(methodDoingCall, methodsBeingCalled)
            }
        }
    }

    fun toObject(): Map<String, Any> {
        return mapOf(
            "magic" to magic,
            "minorVersion" to minorVersion,
            "majorVersion" to majorVersion,
            "constantPool" to constantPool.map { it.toObject() },
            "accessFlags" to accessFlags.map { it.toString() },
            "thisClass" to thisClass.toObject(),
            "superClass" to (superClass?.toObject() ?: "null"),
            "interfaces" to interfaces.map { it.toObject() },
            "fields" to fields.map { it.toObject() },
            "methods" to methods.map { it.toObject() },
            "attributes" to attributes.map { it.toObject() }
        )
    }

    companion object {
        fun fromRawJvmClass(rawJvmClass: RawJvmClass): JvmClass {
            val magic = rawJvmClass.magic.toInt()
            val minorVersion = rawJvmClass.minorVersion.toInt()
            val majorVersion = rawJvmClass.majorVersion.toInt()
            val constantPoolLookup = ConstantPoolLookupImpl(rawJvmClass.constantPool)
            val accessFlags = AccessFlag.fromMask(rawJvmClass.accessFlags.toInt())
            val constantPool = rawJvmClass.constantPool.map { entry ->
                ConstantPoolEntry.from(entry, constantPoolLookup)
            }
            val constantPoolMap = constantPool.associateBy { entry -> entry.raw.index.toInt() }
            val thisClass =
                constantPoolMap.getValue(rawJvmClass.thisClass.toInt()) as ConstantPoolEntry.ConstantPoolEntryClass
            val superClassValue = rawJvmClass.superClass.toInt()
            val superClass =
                if (superClassValue == 0) null else constantPoolMap.getValue(rawJvmClass.superClass.toInt()) as ConstantPoolEntry.ConstantPoolEntryClass
            val interfaces =
                rawJvmClass.interfaces.map { constantPoolMap.getValue(it.toInt()) as ConstantPoolEntry.ConstantPoolEntryClass }
            val fields = rawJvmClass.fields.map { FieldEntry.fromFieldInfo(it, constantPoolMap) }
            val methods = rawJvmClass.methods.map { MethodEntry.fromMethodInfo(it, constantPoolMap) }
            val attributes = rawJvmClass.attributes.map { AttributeEntry.fromAttributeInfo(it, constantPoolMap) }
            return JvmClass(
                magic = magic,
                minorVersion = minorVersion,
                majorVersion = majorVersion,
                constantPool = constantPool,
                accessFlags = accessFlags,
                thisClass = thisClass,
                superClass = superClass,
                interfaces = interfaces,
                fields = fields,
                methods = methods,
                attributes = attributes
            )
        }
    }
}
