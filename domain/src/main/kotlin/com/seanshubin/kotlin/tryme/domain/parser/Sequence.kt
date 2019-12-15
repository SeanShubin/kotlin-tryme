package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class Sequence<T>(
    override val name: String,
    private val lookup: (String) -> Matcher<T>,
    private vararg val matcherNames: String
) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        var currentCursor = cursor
        val trees = mutableListOf<Tree<T>>()
        for (matcherName in matcherNames) {
            val matcher = lookup(matcherName)
            val result = matcher.checkMatch(currentCursor)
            if (result is Failure) {
                return result
            }
            result as Success<T>
            currentCursor = result.positionAfterMatch
            trees.add(result.value)
        }
        return Success(name, Branch(name, trees), currentCursor)
    }
}
