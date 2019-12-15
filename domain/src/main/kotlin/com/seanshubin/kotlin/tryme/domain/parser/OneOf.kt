package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class OneOf<T>(
    override val name: String,
    private val lookup: (String) -> Matcher<T>,
    private vararg val matcherNames: String
) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        for (matcherName in matcherNames) {
            val matcher = lookup(matcherName)
            val result = matcher.checkMatch(cursor)
            if (result is Success<*>) {
                return result
            }
        }
        val joinedNames = matcherNames.joinToString(", ")
        val message = "Expected one of: $joinedNames"
        return Failure(message, cursor)
    }
}
