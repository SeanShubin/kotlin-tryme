package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class ZeroOrMore<T>(
    override val name: String,
    private val lookup: (String) -> Matcher<T>,
    private val matcherName: String
) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        val matcher = lookup(matcherName)
        val successes = mutableListOf<Success<T>>()
        var newCursor = cursor
        do {
            val result = matcher.checkMatch(newCursor)
            if (result is Success) {
                successes.add(result)
                newCursor = result.positionAfterMatch
            }
        } while (result is Success)
        val successTrees = successes.map { it.value }
        return Success(name, Branch(name, successTrees), newCursor)
    }
}
