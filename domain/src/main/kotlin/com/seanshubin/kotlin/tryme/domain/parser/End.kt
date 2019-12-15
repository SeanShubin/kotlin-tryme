package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class End<T>(override val name: String) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        return if (cursor.isEnd) {
            Success(name, Branch(name, emptyList()), cursor)
        } else {
            Failure("End expected", cursor)
        }
    }
}
