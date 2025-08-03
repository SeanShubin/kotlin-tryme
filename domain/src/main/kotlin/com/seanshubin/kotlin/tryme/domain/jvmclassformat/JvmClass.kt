package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class JvmClass(
    val magic: Int,
    val minorVersion: Int,
    val majorVersion: Int,
    val constantPool: List<ConstantPoolEntry>,
    val accessFlags: Set<AccessFlag>,
    val thisClass: ConstantPoolEntry.ConstantPoolEntryClass,
    val superClass: ConstantPoolEntry.ConstantPoolEntryClass,
    val interfaces: List<InterfaceEntry>,
    val fields: List<FieldEntry>,
    val methods: List<MethodEntry>,
    val attributes: List<AttributeEntry>
) {
    val methodInvocationOpCodes = setOf(
        Code.invokestatic,
        Code.invokespecial,
        Code.invokevirtual,
        Code.invokeinterface,
        Code.invokedynamic
    )
    fun methodDependencies():List<Pair<String, List<String>>>{
        return methods.map { method ->
            val className = thisClass.name.raw.value
            val name = method.name.raw.value
            val descriptor = method.descriptor.raw.value
            val codeBlock = method.codeAttribute.codeBlock
            val methodDoingCall = "$className.$name$descriptor"
            val methodsBeingCalled = codeBlock.opCodes.mapNotNull { opCode ->
                if(methodInvocationOpCodes.contains(opCode.code)){
                    opCode as OpCodeEntry.ConstantPoolIndex
                    val constantPoolEntry = opCode.constantPoolEntry
                    when(constantPoolEntry){
                        is ConstantPoolEntry.ConstantPoolEntryMethodref -> {
                            constantPoolEntry.methodAddress()
                        }
                        is ConstantPoolEntry.ConstantPoolEntryInvokeDynamic -> {
                            constantPoolEntry.methodAddress()
                        }
                        else -> {
                            throw RuntimeException("Unexpected constant pool entry type ${constantPoolEntry.javaClass.simpleName}")
                        }
                    }
                } else {
                    null
                }
            }
            Pair(methodDoingCall, methodsBeingCalled)
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
            "superClass" to superClass.toObject(),
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
            val thisClass = constantPoolMap.getValue(rawJvmClass.thisClass.toInt()) as ConstantPoolEntry.ConstantPoolEntryClass
            val superClass = constantPoolMap.getValue(rawJvmClass.superClass.toInt()) as ConstantPoolEntry.ConstantPoolEntryClass
            val interfaces = rawJvmClass.interfaces.map { InterfaceEntry.from(it, constantPoolMap) }
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
