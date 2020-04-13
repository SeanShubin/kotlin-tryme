package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

class OneOfPredicate<T>(override val name: String,
                        private val predicateName: String,
                        private val predicate: (T) -> Boolean) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        return when {
            cursor.isEnd -> {
                Failure("Expected value matching predicate named '$predicateName', got end of input", cursor)
            }
            predicate(cursor.value) -> Success(name, Leaf(name, cursor.value), cursor.next())
            else -> Failure("Expected value matching predicate named '$predicateName', got '${cursor.value}'", cursor)
        }
    }
}
