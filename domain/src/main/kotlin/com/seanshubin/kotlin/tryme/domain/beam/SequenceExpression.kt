package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class SequenceExpression(override val name: String, private val expressions: List<Expression>) : Expression {
    override fun parse(cursor: Cursor<Byte>): Result {
        var currentCursor = cursor
        val list = mutableListOf<Tree<Byte>>()
        expressions.forEach { expression ->
            when (val result = expression.parse(currentCursor)) {
                is Result.Success -> {
                    currentCursor = result.cursor
                    list.add(result.tree)
                }
                is Result.Failure -> {
                    return Result.Failure("$name, unable to parse expression ${expression.name}")
                }
            }
        }
        return Result.Success(currentCursor, Tree.Branch(name, list))
    }
}
