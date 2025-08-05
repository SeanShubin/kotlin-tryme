package com.seanshubin.kotlin.tryme.domain.jvmclassformat

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
        val argCount: Int
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

    data class ArrayType(
        override val index: Int,
        override val code: Code,
        val arrayType: PrimitiveArrayType
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.ARRAY_TYPE
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "arrayType" to arrayType
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

    data class ShortValue(
        override val index: Int,
        override val code: Code,
        val value: Short
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.SHORT_VALUE
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
        val constValue: Int
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

    data class LookupSwitchEntry(
        override val index: Int,
        val padding: Int,
        val default: Int,
        val pairs: List<MatchOffset>
    ) : OpCodeEntry {
        fun size(): Int {
            return 1 + padding + 4 + 4 + pairs.size * 8
        }

        override val code: Code = Code.lookupswitch
        override val codeArgs: CodeArgs = CodeArgs.LOOKUP_SWITCH
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "padding" to padding,
                "default" to default,
                "pairs" to pairs.map { it.toObject() }
            )
        }

        companion object {
            fun fromBytes(index: Int, bytes: List<Byte>): LookupSwitchEntry {
                if (bytes[index] != Code.lookupswitch.opcode.toByte()) throw RuntimeException("Expected ${Code.lookupswitch} at index $index")
                val padding = (-index - 1).mod(4)
                var currentIndex = index + padding + 1
                val default = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val npairs = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val pairs = (0 until npairs).map { i ->
                    val match = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                    currentIndex += 4
                    val offset = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                    currentIndex += 4
                    MatchOffset(match, offset)
                }
                return LookupSwitchEntry(index, padding, default, pairs)
            }
        }
    }

    data class TableSwitchEntry(
        override val index: Int,
        val padding: Int,
        val default: Int,
        val low: Int,
        val high: Int,
        val jumpOffsets: List<Int>
    ) : OpCodeEntry {
        override val code: Code = Code.tableswitch
        override val codeArgs: CodeArgs = CodeArgs.TABLE_SWITCH
        fun size(): Int {
            return 1 + padding + 4 + 4 + 4 + (high - low + 1) * 4
        }

        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "padding" to padding,
                "default" to default,
                "low" to low,
                "high" to high,
                "jumpOffsets" to jumpOffsets
            )
        }

        companion object {
            fun fromBytes(index: Int, bytes: List<Byte>): TableSwitchEntry {
                if (bytes[index] != Code.tableswitch.opcode.toByte()) throw RuntimeException("Expected ${Code.tableswitch} at index $index")
                val padding = (-index - 1).mod(4)
                var currentIndex = index + padding + 1
                val default = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val low = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val high = bytesToInt(bytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val jumpOffsets = (low..high).map { i ->
                    bytesToInt(bytes.slice(currentIndex until currentIndex + 4)).also {
                        currentIndex += 4
                    }
                }
                return TableSwitchEntry(index, padding, default, low, high, jumpOffsets)
            }
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
                        val constantPoolIndex = list[index + 1].toInt() and 0xFF
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("$code $codeArgs Invalid constant pool index: $constantPoolIndex")
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
                        val methodRef =
                            constantPoolMap[constantPoolIndex] as ConstantPoolEntry.ConstantPoolEntryInterfaceMethodref
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

                    CodeArgs.SHORT_VALUE -> {
                        val value = (list[index + 1].toInt() shl 8 or (list[index + 2].toInt() and 0xFF)).toShort()
                        ShortValue(index, code, value)
                    }

                    CodeArgs.INDEX_CONST -> {
                        val localVariableIndex = list[index + 1].toInt()
                        val constValue = list[index + 2].toInt()
                        IndexConst(index, code, localVariableIndex, constValue)
                    }

                    CodeArgs.ARRAY_TYPE -> {
                        val arrayType = PrimitiveArrayType.fromByte(list[index + 1])
                        ArrayType(index, code, arrayType)
                    }

                    CodeArgs.LOOKUP_SWITCH -> {
                        val lookupSwitchEntry = LookupSwitchEntry.fromBytes(index, list)
                        if (lookupSwitchEntry.index != index) {
                            throw IllegalArgumentException("Lookup switch entry index ${lookupSwitchEntry.index} does not match expected index $index")
                        }
                        lookupSwitchEntry
                    }

                    CodeArgs.TABLE_SWITCH -> {
                        val tableSwitchEntry = TableSwitchEntry.fromBytes(index, list)
                        if (tableSwitchEntry.index != index) {
                            throw IllegalArgumentException("Table switch entry index ${tableSwitchEntry.index} does not match expected index $index")
                        }
                        tableSwitchEntry
                    }

                    else -> throw UnsupportedOperationException("$code $codeArgs")
                }
                result.add(entry)
                index += size + 1
            }
            return result
        }

        private fun bytesToInt(bytes: List<Byte>): Int =
            bytes.fold(0) { running, byte -> (running shl 8) or byte.toInt() }
    }
}
