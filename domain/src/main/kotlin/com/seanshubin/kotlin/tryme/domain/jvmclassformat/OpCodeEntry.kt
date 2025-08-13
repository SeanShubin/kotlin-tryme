package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil.bytesToInt

sealed interface OpCodeEntry {
    val index: Int
    val code: Code
    val bytes: List<Byte>
    val codeArgs: CodeArgs
    fun toObject(): Map<String, Any>
    data class NoArgs(override val index: Int, override val code: Code, override val bytes: List<Byte>) : OpCodeEntry {
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
        override val bytes: List<Byte>,
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

    data class ConstantPoolIndexThenDimensions(
        override val index: Int,
        override val code: Code,
        override val bytes: List<Byte>,
        val constantPoolEntry: ConstantPoolEntry,
        val dimensions: Int
    ) : OpCodeEntry {
        override val codeArgs: CodeArgs = CodeArgs.CONSTANT_POOL_INDEX_THEN_DIMENSIONS
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "constantPoolEntry" to constantPoolEntry.toObject(),
                "dimensions" to dimensions
            )
        }
    }

    data class MethodRefAndArgCount(
        override val index: Int,
        override val code: Code,
        override val bytes: List<Byte>,
        val methodRef: ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
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
        override val bytes: List<Byte>,
        val padding: Int,
        val default: Int,
        val pairs: List<MatchOffset>
    ) : OpCodeEntry {
        fun argSize(): Int {
            return padding + 4 + 4 + pairs.size * 8
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
            fun fromBytes(index: Int, methodBytes: List<Byte>): LookupSwitchEntry {
                if (methodBytes[index] != Code.lookupswitch.opcode.toByte()) throw RuntimeException("Expected ${Code.lookupswitch} at index $index")
                val padding = (-index - 1).mod(4)
                var currentIndex = index + padding + 1
                val default = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val npairs = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val pairs = (0 until npairs).map { i ->
                    val match = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                    currentIndex += 4
                    val offset = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                    currentIndex += 4
                    MatchOffset(match, offset)
                }
                val bytes = methodBytes.subList(index, currentIndex)
                return LookupSwitchEntry(index, bytes, padding, default, pairs)
            }
        }
    }

    data class TableSwitchEntry(
        override val index: Int,
        override val bytes: List<Byte>,
        val padding: Int,
        val default: Int,
        val low: Int,
        val high: Int,
        val jumpOffsets: List<Int>
    ) : OpCodeEntry {
        override val code: Code = Code.tableswitch
        override val codeArgs: CodeArgs = CodeArgs.TABLE_SWITCH
        fun argSize(): Int {
            return padding + 4 + 4 + 4 + (high - low + 1) * 4
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
            fun fromBytes(index: Int, methodBytes: List<Byte>): TableSwitchEntry {
                if (methodBytes[index] != Code.tableswitch.opcode.toByte()) throw RuntimeException("Expected ${Code.tableswitch} at index $index")
                val padding = (-index - 1).mod(4)
                var currentIndex = index + padding + 1
                val default = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val low = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val high = bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4))
                currentIndex += 4
                val jumpOffsets = (low..high).map { i ->
                    bytesToInt(methodBytes.slice(currentIndex until currentIndex + 4)).also {
                        currentIndex += 4
                    }
                }
                val bytes = methodBytes.subList(index, currentIndex)
                val result = TableSwitchEntry(index, bytes, padding, default, low, high, jumpOffsets)
                return result
            }
        }
    }

    interface WideEntry:OpCodeEntry {
        fun argSize(): Int
        companion object {
            fun fromBytes(index: Int, methodBytes: List<Byte>): WideEntry {
                if (methodBytes[index] != Code.wide.opcode.toByte()) throw RuntimeException("Expected ${Code.wide} at index $index")
                val wideCode = Code.fromByte(methodBytes[index + 1], index, methodBytes)
                return when (wideCode) {
                    Code.iload, Code.fload, Code.aload, Code.lload, Code.dload, Code.istore, Code.fstore, Code.astore, Code.dstore, Code.ret -> {
                        val localVariableIndex =
                            ByteUtil.bytesToUShort(methodBytes.slice(index + 2 until index + 4)).toInt()
                        WideEntryFormat1(index, wideCode, methodBytes.subList(index, index + 4), localVariableIndex)
                    }

                    Code.iinc -> {
                        val localVariableIndex =
                            ByteUtil.bytesToUShort(methodBytes.slice(index + 2 until index + 4)).toInt()
                        val constValue = ByteUtil.bytesToUShort(methodBytes.slice(index + 4 until index + 6)).toInt()
                        WideEntryFormat2(
                            index,
                            wideCode,
                            methodBytes.subList(index, index + 6),
                            localVariableIndex,
                            constValue
                        )
                    }

                    else -> throw UnsupportedOperationException("Unexpected wide code: $wideCode")
                }
            }
        }
    }

    data class WideEntryFormat1(
        override val index: Int,
        override val code: Code,
        override val bytes: List<Byte>,
        val localVariableIndex: Int
    ) : WideEntry {
        override fun argSize(): Int {
            return 3
        }

        override val codeArgs: CodeArgs = CodeArgs.WIDE
        override fun toObject(): Map<String, Any> {
            return mapOf(
                "index" to index,
                "code" to code.toObject(),
                "localVariableIndex" to localVariableIndex
            )
        }
    }

    data class WideEntryFormat2(
        override val index: Int,
        override val code: Code,
        override val bytes: List<Byte>,
        val localVariableIndex: Int,
        val constValue: Int
    ) : WideEntry {
        override fun argSize(): Int {
            return 5
        }

        override val codeArgs: CodeArgs = CodeArgs.WIDE
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
        fun fromBytes(methodBytes: List<Byte>, constantPoolMap: Map<Int, ConstantPoolEntry>, events:Events): List<OpCodeEntry> {
            val result = mutableListOf<OpCodeEntry>()
            var index = 0
            while (index < methodBytes.size) {
                val codeByte = methodBytes[index]
                val code = Code.fromByte(codeByte, index, methodBytes)
                val codeArgs = code.codeArgs
                val size = codeArgs.lookupArgSize(index, methodBytes)
                val bytes = methodBytes.subList(index, index + size + 1)
                val entry = when (codeArgs) {
                    CodeArgs.NONE -> NoArgs(index, code, bytes)
                    CodeArgs.CONSTANT_POOL_INDEX -> {
                        val constantPoolIndex = methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(index, code, bytes, constantPoolEntry)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_SHORT -> {
                        val constantPoolIndex = methodBytes[index + 1].toInt() and 0xFF
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("$code $codeArgs Invalid constant pool index: $constantPoolIndex")
                        ConstantPoolIndex(index, code, bytes, constantPoolEntry)
                    }

                    CodeArgs.LOCAL_VARIABLE_INDEX -> {
                        val localVariableIndex = methodBytes[index + 1].toInt()
                        LocalVariableIndex(index, code, bytes, localVariableIndex)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES -> {
                        val constantPoolIndex = methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        if (methodBytes[index + 3].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 3} to be 0")
                        if (methodBytes[index + 4].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 4} to be 0")
                        ConstantPoolIndex(index, code, bytes, constantPoolEntry)
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO -> {
                        val constantPoolIndex = methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)
                        val methodRef =
                            constantPoolMap[constantPoolIndex] as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                        val count = methodBytes[index + 3].toInt()
                        if (methodBytes[index + 4].toInt() != 0) throw IllegalArgumentException("Expected byte at index ${index + 4} to be 0")
                        MethodRefAndArgCount(index, code, bytes, methodRef, count)
                    }

                    CodeArgs.BRANCH_OFFSET -> {
                        val offset = methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)
                        BranchOffset(index, code, bytes, offset)
                    }

                    CodeArgs.BYTE_VALUE -> {
                        val value = methodBytes[index + 1]
                        ByteValue(index, code, bytes, value)
                    }

                    CodeArgs.SHORT_VALUE -> {
                        val value = (methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)).toShort()
                        ShortValue(index, code, bytes, value)
                    }

                    CodeArgs.INDEX_CONST -> {
                        val localVariableIndex = methodBytes[index + 1].toInt()
                        val constValue = methodBytes[index + 2].toInt()
                        IndexConst(index, code, bytes, localVariableIndex, constValue)
                    }

                    CodeArgs.ARRAY_TYPE -> {
                        val arrayType = PrimitiveArrayType.fromByte(methodBytes[index + 1])
                        ArrayType(index, code, bytes, arrayType)
                    }

                    CodeArgs.LOOKUP_SWITCH -> {
                        val lookupSwitchEntry = LookupSwitchEntry.fromBytes(index, methodBytes)
                        if (lookupSwitchEntry.index != index) {
                            throw IllegalArgumentException("Lookup switch entry index ${lookupSwitchEntry.index} does not match expected index $index")
                        }
                        lookupSwitchEntry
                    }

                    CodeArgs.TABLE_SWITCH -> {
                        val tableSwitchEntry = TableSwitchEntry.fromBytes(index, methodBytes)
                        if (tableSwitchEntry.index != index) {
                            throw IllegalArgumentException("Table switch entry index ${tableSwitchEntry.index} does not match expected index $index")
                        }
                        tableSwitchEntry
                    }

                    CodeArgs.CONSTANT_POOL_INDEX_THEN_DIMENSIONS -> {
                        val constantPoolIndex = methodBytes[index + 1].toInt() shl 8 or (methodBytes[index + 2].toInt() and 0xFF)
                        val constantPoolEntry = constantPoolMap[constantPoolIndex]
                            ?: throw IllegalArgumentException("Invalid constant pool index: $constantPoolIndex")
                        val dimensions = methodBytes[index + 3].toInt()
                        if (dimensions < 1) {
                            throw IllegalArgumentException("Dimensions must be at least 1, but was $dimensions")
                        }
                        ConstantPoolIndexThenDimensions(index, code, bytes, constantPoolEntry, dimensions)
                    }

                    CodeArgs.WIDE -> {
                        val wideCode = Code.fromByte(methodBytes[index + 1], index, methodBytes)
                        when (wideCode) {
                            Code.iload, Code.fload, Code.aload, Code.lload, Code.dload, Code.istore, Code.fstore, Code.astore, Code.dstore, Code.ret -> {
                                val localVariableIndex =
                                    ByteUtil.bytesToUShort(listOf(methodBytes[index + 2], methodBytes[index + 3])).toInt()
                                WideEntryFormat1(index, code, bytes, localVariableIndex)
                            }

                            Code.iinc -> {
                                val localVariableIndex =
                                    ByteUtil.bytesToUShort(listOf(methodBytes[index + 2], methodBytes[index + 3])).toInt()
                                val constValue =
                                    ByteUtil.bytesToUShort(listOf(methodBytes[index + 4], methodBytes[index + 5])).toInt()
                                WideEntryFormat2(index, code, bytes, localVariableIndex, constValue)
                            }

                            else -> throw UnsupportedOperationException("Unexpected wide code: $wideCode")
                        }
                    }

                    else -> throw UnsupportedOperationException("$code $codeArgs")
                }
                result.add(entry)
                events.opCodeParsed(entry)
                index += size + 1
            }
            return result
        }
    }
}
