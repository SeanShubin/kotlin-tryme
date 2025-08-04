package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import jdk.internal.classfile.constantpool.MethodRefEntry

sealed interface OpCodeEntry {
    val index: Int
    val code: Code
    val codeArgs: CodeArgs
    fun toObject(): Map<String, Any>
    data class NoArgs(override val index: Int, override val code: Code) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.NONE
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject()
            )
        }
    }

    data class ConstantPoolIndex(
        override val index: Int,
        override val code: Code,
        val constantPoolEntry: ConstantPoolEntry
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.CONSTANT_POOL_INDEX
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "constantPoolEntry" to constantPoolEntry.toObject()
            )
        }
    }

    data class MethodRefAndArgCount(
        override val index: Int,
        override val code: Code,
        val methodRef: ConstantPoolEntry.ConstantPoolEntryInterfaceMethodref,
        val argCount:Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.CONSTANT_POOL_INDEX
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "methodRef" to methodRef.toObject(),
                "argCount" to argCount
            )
        }
    }

    data class LocalVariableIndex(
        override val index: Int,
        override val code: Code,
        val localVariableIndex: Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.LOCAL_VARIABLE_INDEX
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "localVariableIndex" to localVariableIndex
            )
        }
    }

    data class BranchOffset(
        override val index: Int,
        override val code: Code,
        val offset: Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.BRANCH_OFFSET
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "offset" to offset
            )
        }
    }

    data class ByteValue(
        override val index: Int,
        override val code: Code,
        val value: Byte
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.BYTE_VALUE
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "value" to value.toInt()
            )
        }
    }

    data class IndexConst(
        override val index: Int,
        override val code: Code,
        val localVariableIndex: Int,
        val constValue:Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.INDEX_CONST
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "localVariableIndex" to localVariableIndex,
                "constValue" to constValue
            )
        }
    }

    companion object {
        fun fromBytes(list: List<Byte>, constantPoolMap: Map<Int, ConstantPoolEntry>): List<OpCodeEntry> {
            val result = mutableListOf<OpCodeEntry>()
            var index = 0
            while (index < list.size) {
                val codeByte = list[index]
                val code = Code.fromByte(codeByte)
                val codeArgs = code.codeArgs
                val size = codeArgs.lookupSize(index, list)
                val entry = when (codeArgs) {
                    CodeArgs.NONE -> NoArgs(index, code)
                    CodeArgs.CONSTANT_POOL_INDEX -> {
                        val constantPoolIndex = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(index, code, constantPoolEntry)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_SHORT -> {
                        val constantPoolIndex = list[index + 1].toInt()
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(index, code, constantPoolEntry)
                    }

                    CodeArgs.LOCAL_VARIABLE_INDEX -> {
                        val localVariableIndex = list[index + 1].toInt()
                        LocalVariableIndex(index, code, localVariableIndex)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES -> {
                        val constantPoolIndex = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        if (list[index + 3].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 3} to be 0")
                        if (list[index + 4].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 4} to be 0")
                        ConstantPoolIndex(index, code, constantPoolEntry)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO -> {
                        val constantPoolIndex = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        val methodRef = constantPoolMap[constantPoolIndex] as ConstantPoolEntry.ConstantPoolEntryInterfaceMethodref
                        val count = list[index + 3].toInt()
                        if (list[index + 4].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 4} to be 0")
                        MethodRefAndArgCount(index, code, methodRef, count)
                    }

                    CodeArgs.BRANCH_OFFSET -> {
                        val offset = list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)
                        BranchOffset(index, code, offset)
                    }

                    CodeArgs.BYTE_VALUE -> {
                        val value = list[index + 1]
                        ByteValue(index, code, value)
                    }

                    CodeArgs.INDEX_CONST -> {
                        val localVariableIndex = list[index + 1].toInt()
                        val constValue = list[index + 2].toInt()
                        IndexConst(index, code, localVariableIndex, constValue)
                    }

                    else -> throw UnsupportedOperationException("$code $codeArgs")
                }
                result.add(entry)
                index += size + 1
            }
            return result
        }
    }
}
