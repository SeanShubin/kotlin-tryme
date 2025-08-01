package com.seanshubin.kotlin.tryme.domain.jvmclassformat

sealed interface OpCodeEntry {
    val code:Code
    val codeArgs: CodeArgs
    fun toObject(): Map<String, Any>
    class NoArgs(override val code: Code) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.NONE
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "code" to code.toObject()
            )
        }
    }
    class ConstantPoolIndex(
        override val code: Code,
        val constantPoolEntry: ConstantPoolEntry
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.CONSTANT_POOL_INDEX
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "code" to code.toObject(),
                "constantPoolEntry" to constantPoolEntry.toObject()
            )
        }
    }
    class LocalVariableIndex(
        override val code: Code,
        val localVariableIndex: Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.LOCAL_VARIABLE_INDEX
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "code" to code.toObject(),
                "localVariableIndex" to localVariableIndex
            )
        }
    }
    companion object{
        fun fromBytes(list:List<Byte>, constantPoolMap: Map<Int, ConstantPoolEntry>):List<OpCodeEntry>{
            val result = mutableListOf<OpCodeEntry>()
            var index = 0
            while (index < list.size) {
                val codeByte = list[index]
                val code = Code.fromByte(codeByte)
                val codeArgs = code.codeArgs
                val size = codeArgs.lookupSize(index, list)
                val entry = when(codeArgs){
                    CodeArgs.NONE -> NoArgs(code)
                    CodeArgs.CONSTANT_POOL_INDEX -> {
                        val constantPoolIndex = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(code, constantPoolEntry)
                    }
                    CodeArgs.CONSTANT_POOL_INDEX_SHORT -> {
                        val constantPoolIndex = list[index + 1].toInt()
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(code, constantPoolEntry)
                    }
                    CodeArgs.LOCAL_VARIABLE_INDEX -> {
                        val localVariableIndex = list[index + 1].toInt()
                        LocalVariableIndex(code, localVariableIndex)
                    }
                    CodeArgs.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES -> {
                        val constantPoolIndex = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        if(list[index+3].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index+3} to be 0")
                        if(list[index+4].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index+4} to be 0")
                        ConstantPoolIndex(code, constantPoolEntry)
                    }
                    else -> throw UnsupportedOperationException("$codeArgs")
                }
                result.add(entry)
                index += size + 1
            }
            return result
        }
    }
}
