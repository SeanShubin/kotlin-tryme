package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.ExpressionUtil.parseMany
import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class ZeroOrMoreExpression(override val name: String, private val expression: Expression) : Expression {
    override fun parse(start: Cursor<Byte>): Result {
        val (newCursor, list) = parseMany(start, expression)
        return Result.Success(newCursor, Tree.Branch(name, list))
    }
}