package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleByteList
import com.seanshubin.kotlin.tryme.domain.beam.Conversions.toInt
import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class DynamicallySizedByteSequenceExpression(
    override val name: String,
    private val sizeSuffix: String,
    private val contentSuffix: String
) : Expression {
    override fun parse(start: Cursor<Byte>): Result {
        var current = start
        val sizeResult = ByteSequenceFixedSizeExpression("$name-$sizeSuffix", 4).parse(current)
        val size = when (sizeResult) {
            is Result.Success -> {
                current = sizeResult.cursor
                extractSize(sizeResult)
            }
            is Result.Failure -> return sizeResult
        }
        val contentResult = ByteSequenceFixedSizeExpression("$name-$contentSuffix", size).parse(current)
        when (contentResult) {
            is Result.Success -> {}
            is Result.Failure -> return contentResult
        }
        val result = sizeResult.merge(name, contentResult)
        return result
    }

    private fun extractSize(success: Result.Success): Int {
        val byteList = assembleByteList(success.tree)
        return byteList.toInt()
    }
}
