package com.seanshubin.kotlin.tryme.domain.parser

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor

interface Matcher<T> {
    val name: String
    fun checkMatch(cursor: Cursor<T>): Result<T>
}
