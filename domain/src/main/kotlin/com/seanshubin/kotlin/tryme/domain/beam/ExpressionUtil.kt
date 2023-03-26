package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

object ExpressionUtil {
    fun parseMany(cursor: Cursor<Byte>, expression: Expression): Pair<Cursor<Byte>, List<Tree<Byte>>> {
        var currentCursor = cursor
        var lastSuccess = true
        val list = mutableListOf<Tree<Byte>>()
        while (lastSuccess) {
            when (val result = expression.parse(currentCursor)) {
                is Result.Success -> {
                    list.add(result.tree)
                    currentCursor = result.cursor
                }
                is Result.Failure -> {
                    lastSuccess = false
                }
            }
        }
        return Pair(currentCursor, list)
    }

    fun consumeManyInterleave(
        cursor: Cursor<Byte>,
        outer: Expression,
        inner: Expression
    ): Pair<Cursor<Byte>, List<Tree<Byte>>> {
        val list = mutableListOf<Tree<Byte>>()
        var currentCursor = cursor
        val firstResult = outer.parse(cursor)
        var lastSuccess = firstResult.success
        if (firstResult is Result.Success) {
            list.add(firstResult.tree)
            currentCursor = firstResult.cursor
        }
        while (lastSuccess) {
            when (val betweenResult = inner.parse(currentCursor)) {
                is Result.Success -> {
                    when (val expressionResult = outer.parse(betweenResult.cursor)) {
                        is Result.Success -> {
                            list.add(betweenResult.tree)
                            list.add(expressionResult.tree)
                            currentCursor = expressionResult.cursor
                        }
                        is Result.Failure -> {
                            lastSuccess = false
                        }
                    }
                }
                is Result.Failure -> {
                    lastSuccess = false
                }
            }
        }
        return Pair(currentCursor, list)
    }
}
