package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor
import kotlin.reflect.KClass

class ValueOfType<T : Any, U : T>(override val name: String, private val expected: KClass<U>) : Matcher<T> {
    override fun checkMatch(cursor: Cursor<T>): Result<T> {
        return if (cursor.value::class == expected) {
            Success(name, Leaf(name, cursor.value), cursor.next())
        } else {
            Failure("Expected '$expected'", cursor)
        }
    }
}
